package reuben.ethicalc.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import reuben.ethicalc.Database.Product;
import reuben.ethicalc.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductBusinessFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductBusinessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductBusinessFragment extends Fragment implements BusinessFragment.OnFragmentInteractionListener,ProductFragment.OnFragmentInteractionListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String BARCODE_NUM = "barcode num";
    private static final String COMPANY_NAME = "company name";
    private static final String MODE = "mode";

    // TODO: Rename and change types of parameters
    private String barcodeNumber;
    private String companyName;
    private int mode;
    private LinearLayout pdtLinearLayout;
    private LinearLayout companyLinearLayout;
    private OnFragmentInteractionListener mListener;
    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mProductsDatabseReference;

    public ProductBusinessFragment() {
        // Required empty public constructor
    }


    public static ProductBusinessFragment newInstance(String barcode, String company, int mode) {
        ProductBusinessFragment fragment = new ProductBusinessFragment();
        Bundle args = new Bundle();
        args.putInt(MODE, mode);
        if (mode==1){ //barcoe number
            args.putString(BARCODE_NUM, barcode);


        } else {
            args.putString(COMPANY_NAME, company);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mode = getArguments().getInt(MODE);
            if (mode==1){ //if there is barcode number no company name
                barcodeNumber = getArguments().getString(BARCODE_NUM);

            }
            else{ //if there is company name no barcode number
                companyName = getArguments().getString(COMPANY_NAME);
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_product_business, container, false);
        companyLinearLayout = rootview.findViewById(R.id.companyLinearLayout);
        pdtLinearLayout = rootview.findViewById(R.id.pdtLinearLayout);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (mode == 1) {//if got barcode make space for a prodcut fragment
            Fragment pdtFrag = new ProductFragment();
            //set arguments for the product
            Bundle pdtBundle = new Bundle();
            pdtBundle.putString(BARCODE_NUM,barcodeNumber);
            pdtFrag.setArguments(pdtBundle);
            transaction.replace(R.id.pdtLinearLayout, pdtFrag);
            transaction.commit();

            //get company name from the barcode number
            mFireBaseDatabase = FirebaseDatabase.getInstance("https://fir-ethicalc.firebaseio.com/");
            mProductsDatabseReference = mFireBaseDatabase.getReference().child("products");
            Query pdtQuery = mProductsDatabseReference.orderByChild("barcode").equalTo(barcodeNumber);
            pdtQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        Product product = dataSnapshot.getValue(Product.class);
                        companyName = product.getCompanyName();
                        //commit company name from what i got from firebase
                        Fragment compFrag = new BusinessFragment();
                        Bundle compBundle = new Bundle();
                        compBundle.putString(COMPANY_NAME,companyName);
                        compFrag.setArguments(compBundle);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.companyLinearLayout, compFrag);
                        transaction.commit();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        else{
            //if mode=0 only have company name no barcode
            Fragment compFrag = new BusinessFragment();
            Bundle compBundle = new Bundle();
            compBundle.putString(COMPANY_NAME,companyName);
            compFrag.setArguments(compBundle);
            transaction.replace(R.id.companyLinearLayout, compFrag);
            transaction.commit();
        }

        //instantiate my linear layouts





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

    @Override
    public void onFragmentInteraction(Uri uri) {

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
