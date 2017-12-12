package reuben.ethicalc.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import reuben.ethicalc.Adapter.CompanyAdapter;
import reuben.ethicalc.Database.Company;
import reuben.ethicalc.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompanyListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompanyListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView companyListView;
    private CompanyAdapter companyAdapter; //adapter to be used when listView is instantiated
    List<Company> companies = new ArrayList<Company>();
    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mCompaniesDatabaseReference;
    private OnFragmentInteractionListener mListener;

    public CompanyListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompanyListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompanyListFragment newInstance(String param1, String param2) {
        CompanyListFragment fragment = new CompanyListFragment();
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
        // Inflate the layout for this fragment\
        getActivity().setTitle("Companies");
        View rootView = inflater.inflate(R.layout.fragment_company_list, container, false);
        companyListView = (ListView) rootView.findViewById(R.id.companylist_listview);
        //setting up my list view
        companyAdapter = new CompanyAdapter(getActivity(),companies);
        companyListView.setAdapter(companyAdapter);
        //grab information from firebase
        mFireBaseDatabase = FirebaseDatabase.getInstance("https://fir-ethicalc.firebaseio.com/");
        mCompaniesDatabaseReference = mFireBaseDatabase.getReference().child("companies");
        final Query companyQuery = mCompaniesDatabaseReference.orderByChild("companyName");
        companyQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        Company company = data.getValue(Company.class);
                        //populate the list that will be attached to my adapter
                        companies.add(company);
                    }
                    companyAdapter.update(companies);
                    companyAdapter.notifyDataSetChanged();
                    companyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            getActivity().setTitle(companies.get(i).getCompanyName());
                            Fragment fragment = new ProductBusinessFragment();
                            Bundle bundle = new Bundle ();
                            bundle.putParcelable("company",companies.get(i));
                            //bundle.putString("company name",companies.get(i).getCompanyName());
                            bundle.putInt("mode",0);
                            fragment.setArguments(bundle);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, fragment);
                            transaction.addToBackStack("");
                            transaction.commit();
                        }
                    });
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
