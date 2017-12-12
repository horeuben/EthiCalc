package reuben.ethicalc.Fragment;

//import android.app.FragmentTransaction;
import android.support.v4.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
//import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import reuben.ethicalc.Adapter.ShopAdapter;
import reuben.ethicalc.Database.Company;
import reuben.ethicalc.Database.Shop;
import reuben.ethicalc.Database.ShopClass;
import reuben.ethicalc.R;
import reuben.ethicalc.Services.GeofenceTransitionsIntentService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GetNearbyShopsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GetNearbyShopsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetNearbyShopsFragment extends Fragment implements LocationListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView shopsListView;
    private OnFragmentInteractionListener mListener;
    PendingIntent mGeofencePendingIntent;
    public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 100;
    private List<Geofence> mGeofenceList;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = "Rafaela";
    LocationRequest mLocationRequest;
    double currentLatitude = 8.5565795, currentLongitude = 76.8810227;
    ArrayList<ShopClass> ShopClasses ;
    private ShopAdapter mshopAdapter;
    Location mylocation;
    public GetNearbyShopsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Get_Nearby_Shops_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GetNearbyShopsFragment newInstance(String param1, String param2) {
        GetNearbyShopsFragment fragment = new GetNearbyShopsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            initGoogleAPIClient();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_nearbyshops, container, false);
        mGeofenceList = new ArrayList<Geofence>();
        int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resp == ConnectionResult.SUCCESS) {
            createGeofences(1.334276,103.962793,100,"Changi City Point");//CCP
            createGeofences(1.340628,103.963193,50,"Campus Centre");//campus centre
            getGeofencingRequest();
        } else {
            Log.e(TAG, "Your Device doesn't support Google Play Services.");
        }
        ShopClasses = new ArrayList<>();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(1000);
        shopsListView = (ListView) rootview.findViewById(R.id.shopsList);
        mshopAdapter = new ShopAdapter(getActivity(),ShopClasses);
        shopsListView.setAdapter(mshopAdapter);
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance("https://fir-ethicalc.firebaseio.com/");
        DatabaseReference mShopsDatabaseReference = mFirebaseDatabase.getReference().child("shops");
        final ArrayList<Shop> Allshops = new ArrayList<>();
        mShopsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("checking",String.valueOf(dataSnapshot.getChildrenCount()));
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Shop shop = data.getValue(Shop.class);
                    Allshops.add(shop);
                }
                for (Shop thisshop : Allshops){
                    String Locationname = thisshop.getShopname();
                    Location newloc = new Location("check");
                    newloc.setLongitude(Double.valueOf(thisshop.getLng()));
                    newloc.setLatitude(Double.valueOf(thisshop.getLat()));
                    double distance = mylocation.distanceTo(newloc);
                    if (distance<1000) {
                        ShopClass newshop = new ShopClass(Locationname,thisshop.getDescription(),thisshop.getPicureurl(),distance);
                        ShopClasses.add(newshop);
                    }}
                sort(ShopClasses);
                mshopAdapter.update(ShopClasses);
                mshopAdapter.notifyDataSetChanged();
                shopsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        ShopClass thisshop = ShopClasses.get(position);
                        final String companyname = thisshop.getDescription();
                        FirebaseDatabase newFireBaseDatabase = FirebaseDatabase.getInstance("https://fir-ethicalc.firebaseio.com/");
                        DatabaseReference newCompaniesDatabaseReference = newFireBaseDatabase.getReference().child("companies");
                        final Query companyQuery = newCompaniesDatabaseReference.orderByChild("companyName");
                        companyQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot data: dataSnapshot.getChildren()){
                                        Company company = data.getValue(Company.class);
                                        if (company.getCompanyName().equals(companyname)){
                                            Fragment fragment = new ProductBusinessFragment();
                                            Bundle bundle = new Bundle ();
                                            bundle.putParcelable("company",company);
                                            bundle.putInt("mode",0);
                                            fragment.setArguments(bundle);

                                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                            transaction.replace(R.id.fragment_container, fragment);
                                            transaction.addToBackStack("");
                                            transaction.commit();

                                        }
                                    }
                                }
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return rootview;
    }
    private void sort(ArrayList<ShopClass> shops ){
        int n = shops.size();
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (shops.get(j).getDistance() > shops.get(j + 1).getDistance()) {
                    ShopClass temp = shops.get(j);
                    shops.set(j, shops.get(j + 1));
                    shops.set(j + 1, temp);
                }
            }
        }
        Log.i(TAG,"sorted");
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void initGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(connectionAddListener)
                .addOnConnectionFailedListener(connectionFailedListener)
                .build();
        mGoogleApiClient.connect();
    }
    private void getCurrentLocation(){
        try{
            mylocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mylocation == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
            } else {
                currentLatitude = mylocation.getLatitude();
                currentLongitude = mylocation.getLongitude();}}
        catch (SecurityException e){
            e.printStackTrace();
        }

    }
    private GoogleApiClient.ConnectionCallbacks connectionAddListener =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    getCurrentLocation();
                    try{
                        LocationServices.GeofencingApi.addGeofences(
                                mGoogleApiClient,
                                getGeofencingRequest(),
                                getGeofencePendingIntent()
                        ).setResultCallback(new ResultCallback<Status>() {

                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()) {

                                } else {
                                    Log.e(TAG, "Registering geofence failed: " + status.getStatusMessage() +
                                            " : " + status.getStatusCode());
                                }
                            }
                        });
                    } catch (SecurityException securityException) {
                        Log.e(TAG, "Error");
                    }
                }
                @Override
                public void onConnectionSuspended(int i) {
                    Log.e(TAG, "onConnectionSuspended");
                }
            };

    private GoogleApiClient.OnConnectionFailedListener connectionFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    Log.e(TAG, "onConnectionFailed");
                }
            };


    /**
     * Create a Geofence list
     */
    public void createGeofences(double latitude, double longitude,int r,String id) {

        Geofence fence = new Geofence.Builder()
                .setRequestId(id)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setCircularRegion(latitude, longitude, r)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
        mGeofenceList.add(fence);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(getActivity(), GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(getActivity(), 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
    }

}
