package reuben.ethicalc.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    private SearchView companySearchView;
    private ListView companyListView;
    private CompanyAdapter companyAdapter; //adapter to be used when listView is instantiated
    private CompanyAdapter searchAdapter; //adapter to be used by search bar
    List<Company> companies;
    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mCompaniesDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private OnFragmentInteractionListener mListener;
    private List<String> companyNames;

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
        View rootView = inflater.inflate(R.layout.fragment_company_list, container, false);
        companyListView = (ListView) rootView.findViewById(R.id.companylist_listview);
        companySearchView = (SearchView) rootView.findViewById(R.id.companylist_searchview);

        //setting up my list view

        //grab information from firebase
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mCompaniesDatabaseReference = mFireBaseDatabase.getReference().child("companies");
        Query companyQuery = mCompaniesDatabaseReference.orderByChild("companyName");
        companyQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        Company company = data.getValue(Company.class);
                        //populate the list that will be attached to my adapter
                        companies.add(company);
                        //populate list of company names so i can implement my autocomplete
                        companyNames.add(company.getCompanyName());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        companyAdapter = new CompanyAdapter(getActivity(),companies);
        companyListView.setAdapter(companyAdapter);


        //setting up my search bar
        companySearchView.setIconified(false); //so you can directly enter you search
        companySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { //so the list view changes dynamically with what you enter in the search view

            List<Company> allMatches = new ArrayList<Company>();
            @Override
            public boolean onQueryTextSubmit(String s) { //to spell check user's input
                for (String possibleMatch: companyNames){
                    double levenDist = (double) LevenshteinDistance(s, possibleMatch);
                    double longestSubseq = (double) LongestLCS(s, possibleMatch);
                    Double normalizedScore = (((possibleMatch.length() - levenDist) / possibleMatch.length()) * 0.3) + (longestSubseq / possibleMatch.length() * 0.7);
                    Log.i("norm score","norm score of "+possibleMatch+" is "+Double.toString(normalizedScore));
                    //if normalized score is >50% that's a match!
                    if (normalizedScore >= 0.5) {
                        for (Company c:companies){
                            if (c.getCompanyName().equals(possibleMatch)){
                                allMatches.add(c); //add companies whose name matches the possible match into the list of match rsults
                            }
                        }
                    }
                }

                if (allMatches.size() == 0) {
                    //prediction returns 0
                    Toast.makeText(getActivity(), "No results found", Toast.LENGTH_SHORT).show();
                } else {
                    searchAdapter = new CompanyAdapter(getActivity(),allMatches);
                    companyListView.setAdapter(searchAdapter);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Company> tempCompanies = new ArrayList<>();
                for (Company c:companies){
                    if (c.getCompanyName().contains(s.toLowerCase())){ //ignore capitalization, if name contains my entry words i change list adapter
                        tempCompanies.add(c);
                    }
                }
                //make a new adapter for the company list
                CompanyAdapter searchVCompanyAdapter = new CompanyAdapter(getActivity(),tempCompanies);
                searchVCompanyAdapter.notifyDataSetChanged();
                companyListView.setAdapter(searchVCompanyAdapter);
                return true;
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


    //Levenshtien distance calculates the minimum number of insertions, deletions and substitutions to get from one word to the next
    //we will use it to predict what local names the user is trying to spell in the search view
    private int LevenshteinDistance(String Userinput, String Answer){
        char[] UserinputChar = Userinput.toCharArray();
        char[] AnswerChar = Answer.toCharArray();

        int[][] LevenshteinMat = new int [UserinputChar.length+1][AnswerChar.length+1];
        for (int i=0; i<=UserinputChar.length;i++){
            LevenshteinMat[i][0]=i;
        }
        for (int j=0; j<=AnswerChar.length;j++){
            LevenshteinMat[0][j]=j;
        }
        int substitutionCost;
        for (int l=1;l<=Userinput.length();l++){
            for (int k=1;k<=Answer.length();k++){
                if (UserinputChar[l-1]==AnswerChar[k-1]){
                    substitutionCost=0;
                }
                else{
                    substitutionCost=1;
                }
                LevenshteinMat[l][k] = Collections.min(Arrays.asList(LevenshteinMat[l-1][k]+1,
                        LevenshteinMat[l][k-1]+1, LevenshteinMat[l-1][k-1]+substitutionCost));
            }
        }
        return LevenshteinMat[UserinputChar.length][AnswerChar.length];
    }

    //longest common subsequence. subsequence need not be consecutive so it works great for predicting text especially since users may insert or delete certain letters
    //from the place name they intend to spell
    private int LongestLCS(String Userinput, String Answer){
        //ignore capitalization
        char[] UserinputChar = Userinput.toLowerCase().toCharArray();
        char[] AnswerChar = Answer.toLowerCase().toCharArray();

        int[][] LCSMat = new int [UserinputChar.length+1][AnswerChar.length+1];
        for (int i=0; i<=UserinputChar.length;i++){
            LCSMat[i][0]=0;
        }
        for (int j=0; j<=AnswerChar.length;j++){
            LCSMat[0][j]=0;
        }
        for (int l=1;l<=Userinput.length();l++){
            for (int k=1;k<=Answer.length();k++) {
                if (UserinputChar[l-1]==AnswerChar[k-1]){
                    LCSMat[l][k]=LCSMat[l-1][k-1]+1;
                }
                else{
                    LCSMat[l][k]=Collections.max(Arrays.asList(LCSMat[l][k-1],LCSMat[l-1][k]));
                }
            }
        }
        return LCSMat[UserinputChar.length][AnswerChar.length];
    }


}
