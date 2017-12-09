package reuben.ethicalc.Activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import reuben.ethicalc.R;
import reuben.ethicalc.Services.GeofenceTransitionsIntentService;

public class MainActivity2 extends AppCompatActivity  implements LocationListener {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ShareDialog shareDialog;
    private Button fbButton, logoutButton,testButton;
    private CallbackManager callbackManager;

    public static final String KEY = "Geofence";
    PendingIntent mGeofencePendingIntent;
    public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 100;
    private List<Geofence> mGeofenceList;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = "Rafaela";
    LocationRequest mLocationRequest;
    double currentLatitude = 8.5565795, currentLongitude = 76.8810227;
    Boolean locationFound;
    protected LocationManager locationManager;
    protected com.google.android.gms.location.LocationListener locationListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        shareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();
        fbButton = (Button)findViewById(R.id.button_fb);
        logoutButton = (Button)findViewById(R.id.button_logout);
        testButton = (Button)findViewById(R.id.button_test);
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Using EthiCalc!")
                     //     .setImageUrl(Uri.parse("okLoginButton-min-300x136.png"))
                            .setContentDescription("Hey whats up?")
                          .setContentUrl(Uri.parse("www.google.com"))

                            .build();
                    shareDialog.show(linkContent);  // Show facebook ShareDialog
                }
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity2.this,LoginActivity.class));
                finish();
            }
        });
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity2.this, MainActivity.class));

            }
        });
        mGeofenceList = new ArrayList<Geofence>();

        int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resp == ConnectionResult.SUCCESS) {

            initGoogleAPIClient();

            //createGeofences(currentLatitude, currentLongitude);
            createGeofences(1.334276,103.962793,100,"Changi City Point");//CCP
            createGeofences(1.340628,103.963193,50,"Campus Centre");//campus centre
            createGeofences(1.342345, 103.962187,100,"one world");
            getGeofencingRequest();
        } else {
            Log.e(TAG, "Your Device doesn't support Google Play Services.");
        }

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1*1000)        // 100 milliseconds
                .setFastestInterval(1*1000); // 100 milliseconds

    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void initGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(connectionAddListener)
                .addOnConnectionFailedListener(connectionFailedListener)
                .build();
        mGoogleApiClient.connect();
    }

    private GoogleApiClient.ConnectionCallbacks connectionAddListener =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    Log.i(TAG, "onConnected");
                    try{
                        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                        if (location == null) {
                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MainActivity2.this);
                        } else {
                            //If everything went fine lets get latitude and longitude
                            currentLatitude = location.getLatitude();
                            currentLongitude = location.getLongitude();}}
                    catch (SecurityException e){
                        e.printStackTrace();
                    }

                    try{
                        LocationServices.GeofencingApi.addGeofences(
                                mGoogleApiClient,
                                getGeofencingRequest(),
                                getGeofencePendingIntent()
                        ).setResultCallback(new ResultCallback<Status>() {

                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()) {
                                    Log.i(TAG, "Saving Geofence");

                                } else {
                                    Log.e(TAG, "Registering geofence failed: " + status.getStatusMessage() +
                                            " : " + status.getStatusCode());
                                }
                            }
                        });

                    } catch (SecurityException securityException) {
                        // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
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
        //Log.i(TAG,"inside the building");
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
            Log.i(TAG,"intent has value");
            return mGeofencePendingIntent;
        }
        Log.i(TAG,"intent=null");
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        Log.i(TAG, "onLocationChanged");
        //Toast.makeText(this, "location changed!", Toast.LENGTH_SHORT).show();
    }
    public void getcurrent(View view) {
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MainActivity2.this);
            } else {
                //If everything went fine lets get latitude and longitude
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
            }
        Intent todis = new Intent(this, RecyclerActivity.class);
        Log.i(TAG, currentLatitude + "lng" + currentLongitude);
        todis.putExtra(KEY, currentLatitude + ":" + currentLongitude);
        startActivity(todis);
        }
        catch(SecurityException e){
            e.printStackTrace();
        }

    }


}
