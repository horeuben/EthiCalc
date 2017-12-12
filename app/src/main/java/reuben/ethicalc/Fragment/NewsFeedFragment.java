package reuben.ethicalc.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import reuben.ethicalc.Adapter.NewsAdapter;
import reuben.ethicalc.Database.NewsItem;
import reuben.ethicalc.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFeedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //This is the URL i will use to request data from News API
    private final String reqURL="https://newsapi.org/v2/everything?q=sustainable%20business%20practices&sortBy=popularity&apiKey=1e128baae3034bd899a7a791748e8e47";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView newsItemListView;
    public static NewsAdapter newsItemadapter;
    private OnFragmentInteractionListener mListener;
//    private RecyclerView recyclerView;
//    private NewsRAdapter newsRAdapter;
//    private ArrayList<NewsItem> newsItems;


    public NewsFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFeedFragment.
     */
    // TODO: Rename and change types and number of parameters


    public static NewsFeedFragment newInstance(String param1, String param2) {
        NewsFeedFragment fragment = new NewsFeedFragment();
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

        //FOR DEBUGGING Toast.makeText(getActivity(), "fragment", Toast.LENGTH_SHORT).show();
        View rootView = inflater.inflate(R.layout.fragment_news_feed,container,false);
        newsItemListView = (ListView) rootView.findViewById(R.id.newsList);
//        recyclerView = rootView.findViewById(R.id.recyclerView);
//        newsItems = new ArrayList<>();
//        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        newsRAdapter = new NewsRAdapter(newsItems,getActivity());
//        recyclerView.setAdapter(newsRAdapter);
        new NewsFeedFragment.AsyncHttpTask().execute();

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

    public class AsyncHttpTask extends AsyncTask<Void, Void, Void> {
        List<NewsItem> articleList = new ArrayList<>();


        @Override
        protected Void doInBackground(Void... voids) {
            URL url;
            HttpURLConnection urlConnection = null;

            try{
                //using the URL above (from NEWs API)
                url = new URL(reqURL);
                urlConnection = (HttpURLConnection)url.openConnection();
                String response = streamtoString(urlConnection.getInputStream());
                //I will get a list of NewsItem Objects
                articleList = parseResult(response);
           //     newsItems = (ArrayList<NewsItem>) parseResult(response);
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            // Toast.makeText(getActivity(), "async task post execute", Toast.LENGTH_SHORT).show()

            //Once I have finished getting the list of Newsitem objects
            //I attach it to my adapter
            newsItemadapter = new NewsAdapter(getActivity(),articleList);
            newsItemListView.setAdapter(newsItemadapter);
            //this on on Click Listener means when people click on one list item they will got to the news link
            newsItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Uri webpage = Uri.parse(articleList.get(position).getNewsLink());
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(webIntent);
                }
            });
//            newsRAdapter.update(newsItems);
//            newsRAdapter.notifyDataSetChanged();
        }
    }




    private String streamtoString(InputStream stream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String data;
        String result = "";
        try {
            while ((data = bufferedReader.readLine()) != null) {
                result += data;
            }
            if (null != stream){
                stream.close();
            }
        } catch (Exception e){
            e.printStackTrace();;
        }
        return result;
    }

    private List<NewsItem> parseResult(String result){
        JSONObject response = null;
        List<NewsItem> newsItemList = new ArrayList<NewsItem>();
        String totalRes="";
        try {
            response = new JSONObject(result);
            JSONArray articles = response.optJSONArray("articles");
            //test for first jSON object
            int length = 10;
            if (articles.length()<10) length = articles.length();
            for (int i=0; i<length;i++){
                JSONObject article = articles.optJSONObject(i);
                String title = article.optString("title");
                String imageURL = article.optString("urlToImage");
                String articleLink = article.optString("url");
                //to check if I am getting any news titles
                newsItemList.add(new NewsItem(title, imageURL,articleLink));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsItemList;
    }


}
