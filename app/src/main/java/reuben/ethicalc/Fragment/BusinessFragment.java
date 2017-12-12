package reuben.ethicalc.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import reuben.ethicalc.Database.Company;
import reuben.ethicalc.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BusinessFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BusinessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusinessFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String COMPANY_NAME = "company name";

    // TODO: Rename and change types of parameters
    private String businessName;
    private ImageView companyLogo;
    private TextView companyName;
    private TextView companyType;
    private ArcProgress companyCSR;
    private ArcProgress companyEnvironment;
    private ArcProgress companyCommunity;
    private ArcProgress companyEmployee;
    private ArcProgress companyGovernance;
    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mProductsDatabseReference;

    private OnFragmentInteractionListener mListener;

    public BusinessFragment() {
        // Required empty public constructor
    }


    public static BusinessFragment newInstance(String businessName) {
        BusinessFragment fragment = new BusinessFragment();
        Bundle args = new Bundle();
        args.putString(COMPANY_NAME, businessName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            businessName = getArguments().getString(COMPANY_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_impact,container,false);
        companyLogo = (ImageView) rootView.findViewById(R.id.business_imageview_logo);
        companyName = (TextView) rootView.findViewById(R.id.impact_textview_name);
        companyType = (TextView) rootView.findViewById(R.id.business_textview_type);
        companyCSR = (ArcProgress) rootView.findViewById(R.id.business_progressbar_csr);
        companyEnvironment = (ArcProgress) rootView.findViewById(R.id.impact_progressbar_impact);
        companyCommunity = (ArcProgress) rootView.findViewById(R.id.business_progressbar_community);
        companyEmployee = (ArcProgress) rootView.findViewById(R.id.business_progressbar_employees);
        companyGovernance = (ArcProgress) rootView.findViewById(R.id.business_progressbar_governance);

        mFireBaseDatabase = FirebaseDatabase.getInstance("https://fir-ethicalc.firebaseio.com/");
        mProductsDatabseReference = mFireBaseDatabase.getReference().child("products");
        Query companyQuery = mProductsDatabseReference.orderByChild("companyName").equalTo(businessName);
        companyQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data :dataSnapshot.getChildren()) {
                        Company company = data.getValue(Company.class);
                        companyName.setText(company.getCompanyName());
                        companyType.setText(company.getCompanyType());
                        companyCSR.setProgress(Integer.parseInt(company.getCSRRating()));
                        companyEnvironment.setProgress(Integer.parseInt(company.getEnvironmentRating()));
                        companyCommunity.setProgress(Integer.parseInt(company.getCommunityRating()));
                        companyEmployee.setProgress(Integer.parseInt(company.getEmployeesRating()));
                        companyGovernance.setProgress(Integer.parseInt(company.getGovernanceRating()));
                        Picasso.with(getActivity())
                                .load(company.getPictureUrl())
                                .fit()
                                .into(companyLogo);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return rootView;
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
