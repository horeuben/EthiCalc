package reuben.ethicalc.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

import reuben.ethicalc.Database.User;
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

    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private FirebaseAuth mFirebaseAuth;

    private double impact;
    private double impStart;
    private double lastOneWeek;
    private double lastTwoWeek;
    private double lastThreeWeek;
    private double lastFourWeek;
    private double delta;
    private double accum;

    private FirebaseUser user;
    private ImageView dp;
    private TextView name;

    private ArcProgress impactBar;
    private GraphView graph;
    private TextView impactDeltaText;
    private TextView trees;
    private TextView charity;
    private TextView wages;
    private TextView waste;

    private Button shareImpact;
    private Button shareDelta;
    private Button shareTrees;
    private Button shareCharity;
    private Button shareWages;
    private Button shareWaste;
    private TextView shareTemplate;
    private ShareDialog shareDialog;
    
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
        getActivity().setTitle("Your Impact");
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

        impactBar = (ArcProgress) rootView.findViewById(R.id.impact_progressbar_environment);
        graph = (GraphView) rootView.findViewById(R.id.impact_graph_impactdelta);
        impactDeltaText = (TextView) rootView.findViewById(R.id.impact_textview_impactchangevalue);
        trees = (TextView) rootView.findViewById(R.id.impact_textview_equivtreevalue);
        charity = (TextView) rootView.findViewById(R.id.impact_textview_equivcharityvalue);
        wages = (TextView) rootView.findViewById(R.id.impact_textview_equivwagesvalue);
        waste = (TextView) rootView.findViewById(R.id.impact_textview_equivwastevalue);

        mFirebaseAuth = FirebaseAuth.getInstance();
        final String uid = mFirebaseAuth.getUid();
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFireBaseDatabase.getReference().child("users");
        mUsersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)) {
                    impact = Double.valueOf(dataSnapshot.child(uid).child("Impact").getValue().toString());
                    impStart = Double.valueOf(dataSnapshot.child(uid).child("ImpStart").getValue().toString());
                    lastOneWeek = Double.valueOf(dataSnapshot.child(uid).child("last1Week").getValue().toString());
                    lastTwoWeek = Double.valueOf(dataSnapshot.child(uid).child("last2Week").getValue().toString());
                    lastThreeWeek = Double.valueOf(dataSnapshot.child(uid).child("last3Week").getValue().toString());
                    lastFourWeek = Double.valueOf(dataSnapshot.child(uid).child("last4Week").getValue().toString());
                    delta = Double.valueOf(dataSnapshot.child(uid).child("Delta").getValue().toString());
                    accum = Double.valueOf(dataSnapshot.child(uid).child("Accum").getValue().toString());


                    impactBar.setProgress((int) impact);

                    Calendar calendar = Calendar.getInstance();
                    Date d1 = calendar.getTime();

                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                            new DataPoint(graphDateGen(28), lastFourWeek),
                            new DataPoint(graphDateGen(20), lastThreeWeek),
                            new DataPoint(graphDateGen(13), lastTwoWeek),
                            new DataPoint(graphDateGen(6), lastOneWeek),
                            new DataPoint(graphDateGen(0), impStart),
                            new DataPoint(d1, impact)
                    });
                    series.setAnimated(true);
                    series.setColor(Color.parseColor("#ff4081"));
                    series.setDrawDataPoints(true);
                    series.setThickness(8);
                    series.setDataPointsRadius(10);

//                    graph.getViewport().setXAxisBoundsManual(true);
//                    graph.getViewport().setMinX(graphDateGen(28).getTime());
//                    graph.getViewport().setMaxX(d1.getTime());
//
//                    graph.getViewport().setYAxisBoundsManual(true);
//                    graph.getViewport().setMinY(0);
//                    graph.getViewport().setMaxY(100);

                    graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
                    graph.getGridLabelRenderer().setNumHorizontalLabels(3);
//                    graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
                    graph.getGridLabelRenderer().setGridColor(Color.parseColor("#00A5A1"));
                    graph.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#00A5A1"));
                    graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#00A5A1"));
//                    graph.getGridLabelRenderer().setHumanRounding(false);

//                    graph.getGridLabelRenderer().setVerticalLabelsVisible(true);
//                    graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);

                    graph.addSeries(series);


                    if (delta >= 0) {
                        String deltaString = "+" + Double.toString(delta);
                        impactDeltaText.setText(String.valueOf(deltaString));
                    }
                    else {
                        impactDeltaText.setText(String.valueOf(Double.toString(delta)));
                    }


                    int treesInt = (int) accum/500;
                    trees.setText(String.valueOf(treesInt));


                    int charityInt = (int) accum/200;
                    charity.setText(String.valueOf(charityInt));


                    int wagesInt = (int) accum/400;
                    wages.setText(String.valueOf(wagesInt));


                    int wasteInt = (int) accum/300;
                    waste.setText(String.valueOf(wasteInt));
                }

                else {
                    mUsersDatabaseReference.child(uid).setValue(new User(user.getDisplayName(),"0","50","50","0","0","0","50","50","50","50"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        shareImpact = (Button) rootView.findViewById(R.id.impact_button_impactshare);
        shareDelta = (Button) rootView.findViewById(R.id.impact_button_changeshare);
        shareTrees = (Button) rootView.findViewById(R.id.impact_button_treeshare);
        shareCharity = (Button) rootView.findViewById(R.id.impact_button_charityshare);
        shareWages = (Button) rootView.findViewById(R.id.impact_button_wagesshare);
        shareWaste = (Button) rootView.findViewById(R.id.impact_button_wasteshare);
        shareTemplate = (TextView) rootView.findViewById(R.id.impact_textview_sharetemplate);
        shareDialog = new ShareDialog(this);

        shareImpact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTemplate.setBackground(getResources().getDrawable(R.drawable.share_impact));
                shareTemplate.setText(String.valueOf(impactBar.getProgress()));
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    Bitmap image = getBitmapFromView(shareTemplate);
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(image)
                            .build();
                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                    shareDialog.show(content);
                } else {
                    Toast.makeText(view.getContext(), "You need facebook installed!", Toast.LENGTH_LONG).show();
                }
            }
        });

        shareDelta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTemplate.setBackground(getResources().getDrawable(R.drawable.share_delta));
                shareTemplate.setText(String.valueOf(impactDeltaText.getText()));
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    Bitmap image = getBitmapFromView(shareTemplate);
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(image)
                            .build();
                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                    shareDialog.show(content);
                } else {
                    Toast.makeText(view.getContext(), "You need facebook installed!", Toast.LENGTH_LONG).show();
                }
            }
        });

        shareTrees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTemplate.setBackground(getResources().getDrawable(R.drawable.share_trees));
                shareTemplate.setText(String.valueOf(trees.getText()));
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    Bitmap image = getBitmapFromView(shareTemplate);
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(image)
                            .build();
                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                    shareDialog.show(content);
                } else {
                    Toast.makeText(view.getContext(), "You need facebook installed!", Toast.LENGTH_LONG).show();
                }
            }
        });

        shareCharity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTemplate.setBackground(getResources().getDrawable(R.drawable.share_charity));
                shareTemplate.setText(String.valueOf(charity.getText()));
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    Bitmap image = getBitmapFromView(shareTemplate);
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(image)
                            .build();
                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                    shareDialog.show(content);
                } else {
                    Toast.makeText(view.getContext(), "You need facebook installed!", Toast.LENGTH_LONG).show();
                }
            }
        });

        shareWages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTemplate.setBackground(getResources().getDrawable(R.drawable.share_wages));
                shareTemplate.setText(String.valueOf(wages.getText()));
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    Bitmap image = getBitmapFromView(shareTemplate);
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(image)
                            .build();
                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                    shareDialog.show(content);
                } else {
                    Toast.makeText(view.getContext(), "You need facebook installed!", Toast.LENGTH_LONG).show();
                }
            }
        });

        shareWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTemplate.setBackground(getResources().getDrawable(R.drawable.share_waste));
                shareTemplate.setText(String.valueOf(waste.getText()));
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    Bitmap image = getBitmapFromView(shareTemplate);
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(image)
                            .build();
                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                    shareDialog.show(content);
                } else {
                    Toast.makeText(view.getContext(), "You need facebook installed!", Toast.LENGTH_LONG).show();
                }
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

    private Date graphDateGen(int daysBack) {
        final Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        cal.setTime(new Date());
        int today = cal.get(Calendar.DAY_OF_WEEK) + daysBack;
        cal.add(Calendar.DAY_OF_WEEK, -today+Calendar.SUNDAY);
        return cal.getTime();
    }

    // we have a textview with a background as image, and we want to create a bitmap through it to share from facebook when using
    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        view.getBackground().setAlpha(250);// set transparency of image
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
}
