package reuben.ethicalc.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import reuben.ethicalc.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImpactFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImpactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImpactFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseUser user;
    private ImageView dp;
    private TextView name;

    private GraphView graph;

    private OnFragmentInteractionListener mListener;

    public ImpactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImpactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImpactFragment newInstance(String param1, String param2) {
        ImpactFragment fragment = new ImpactFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_impact,container,false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        dp = (ImageView) rootView.findViewById(R.id.impact_imageview_dp);

        Picasso.with(rootView.getContext()).load(user.getPhotoUrl()).into(dp, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) dp.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        dp.setImageDrawable(imageDrawable);
                    }

                    @Override
                    public void onError() {
                        dp.setImageResource(R.drawable.ic_launcher_foreground);
                    }
                });

        name = (TextView) rootView.findViewById(R.id.impact_textview_name);

        name.setText(user.getDisplayName());

        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();

        graph = (GraphView) rootView.findViewById(R.id.impact_graph_impactdelta);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(graphDateGen(28), 1),
                new DataPoint(graphDateGen(20), 5),
                new DataPoint(graphDateGen(13), 3),
                new DataPoint(graphDateGen(6), 2),
                new DataPoint(graphDateGen(0), 6),
                new DataPoint(d1, 9)
        });
        series.setAnimated(true);
        series.setColor(Color.parseColor("#ff4081"));
        series.setDrawDataPoints(true);
        series.setThickness(8);
        series.setDataPointsRadius(10);

        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(6);

        graph.getViewport().setMinX(graphDateGen(28).getTime());
        graph.getViewport().setMaxX(d1.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getGridLabelRenderer().setGridColor(Color.parseColor("#00A5A1"));
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#00A5A1"));
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#00A5A1"));
        graph.getGridLabelRenderer().setHighlightZeroLines(false);

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

    private Date graphDateGen(int daysBack) {
        final Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        cal.setTime(new Date());
        int today = cal.get(Calendar.DAY_OF_WEEK) + daysBack;
        cal.add(Calendar.DAY_OF_WEEK, -today+Calendar.SUNDAY);
        return cal.getTime();
    }
}
