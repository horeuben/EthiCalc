package reuben.ethicalc.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import reuben.ethicalc.Activity.MainActivity;
import reuben.ethicalc.Adapter.CartAdapter;
import reuben.ethicalc.Database.Company;
import reuben.ethicalc.Database.Product;
import reuben.ethicalc.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView  cartListV;
    private CartAdapter cartAdapter;
    private Button calImpact;


    private OnFragmentInteractionListener mListener;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_cart, container, false);
        cartListV = (ListView) rootview.findViewById(R.id.cart_list);
        cartAdapter = new CartAdapter(getActivity(), MainActivity.pdtInCart);
        calImpact = (Button) rootview.findViewById(R.id.calculate_button);
        cartListV.setAdapter(cartAdapter);

        calImpact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase mFireBaseDatabase = FirebaseDatabase.getInstance("https://fir-ethicalc.firebaseio.com/");
                final DatabaseReference mUsersDatabaseReference = mFireBaseDatabase.getReference().child("users");
                FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                final String uid = mFirebaseAuth.getUid();
                mUsersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(uid)) {
                            //todo: here is how you get the value of an attribute
                            double cost = Double.valueOf(dataSnapshot.child(uid).child("Cost").getValue().toString());
                            double impact = Double.valueOf(dataSnapshot.child(uid).child("Impact").getValue().toString());
                            double impStart = Double.valueOf(dataSnapshot.child(uid).child("ImpStart").getValue().toString());
                            double accum = Double.valueOf(dataSnapshot.child(uid).child("Accum").getValue().toString());
                            double accumWeek = Double.valueOf(dataSnapshot.child(uid).child("AccumWeek").getValue().toString());
                            double costNew;
                            double delta;
                            for (Product p: MainActivity.pdtInCart) {
                                costNew = cost + Double.valueOf(p.getMSRP());
                                if (costNew > 1000) costNew = 1000;
                                impact = ((impact * cost) + (Double.valueOf(p.getCSRRating()) * Double.valueOf(p.getMSRP()))) / costNew;
                                cost = costNew;
                                accum += Double.valueOf(p.getCSRRating()) * Double.valueOf(p.getMSRP());
                                accumWeek += Double.valueOf(p.getCSRRating()) * Double.valueOf(p.getMSRP());
                            }
                            MainActivity.pdtInCart.clear();
                            delta = impact - impStart;
                            mUsersDatabaseReference.child(uid).child("Cost").setValue(cost);
                            mUsersDatabaseReference.child(uid).child("Impact").setValue(impact);
                            mUsersDatabaseReference.child(uid).child("Accum").setValue(accum);
                            mUsersDatabaseReference.child(uid).child("AccumWeek").setValue(accumWeek);
                            mUsersDatabaseReference.child(uid).child("Delta").setValue(delta);

                            Fragment fragment = new ImpactFragment();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, fragment);
                            transaction.commit();
                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        return rootview;
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
}
