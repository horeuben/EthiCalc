package reuben.ethicalc.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import reuben.ethicalc.Database.Product;
import reuben.ethicalc.Database.User;
import reuben.ethicalc.Fragment.BusinessFragment;
import reuben.ethicalc.Fragment.CartFragment;
import reuben.ethicalc.Fragment.CompanyListFragment;
import reuben.ethicalc.Fragment.GetNearbyShopsFragment;
import reuben.ethicalc.Fragment.ImpactFragment;
import reuben.ethicalc.Fragment.NewsFeedFragment;
import reuben.ethicalc.Fragment.ProductBusinessFragment;
import reuben.ethicalc.Fragment.ProductFragment;
import reuben.ethicalc.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GetNearbyShopsFragment.OnFragmentInteractionListener,
        NewsFeedFragment.OnFragmentInteractionListener,ImpactFragment.OnFragmentInteractionListener,CompanyListFragment.OnFragmentInteractionListener, ProductBusinessFragment.OnFragmentInteractionListener,
        ProductFragment.OnFragmentInteractionListener,BusinessFragment.OnFragmentInteractionListener, CartFragment.OnFragmentInteractionListener{

    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser user;
    private ImageView imageViewProfilePic, imageViewStarIcon;
    private String barcodeNumber;
    public static List<Product> pdtInCart = new ArrayList<Product>();
    private TextView textViewName, textViewImpact;
    private Button fab;

    private double impact;

    private static final int MY_LOCATION_REQUEST_CODE = 9;


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("News Feed");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new NewsFeedFragment()).commit();
        //Check location permission for sdk >= 23
        if (Build.VERSION.SDK_INT >= 23) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request permission
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_REQUEST_CODE);
            }
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View naviheaderview = navigationView.getHeaderView(0);
        imageViewProfilePic = (ImageView)naviheaderview.findViewById(R.id.imageViewProfilePic);
        imageViewStarIcon = (ImageView)naviheaderview.findViewById(R.id.imageViewStarIcon);
        textViewName = (TextView) naviheaderview.findViewById(R.id.textViewName);

        Picasso.with(this).load(user.getPhotoUrl()).into(imageViewProfilePic, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap imageBitmap = ((BitmapDrawable) imageViewProfilePic.getDrawable()).getBitmap();
                RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                imageDrawable.setCircular(true);
                imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                imageViewProfilePic.setImageDrawable(imageDrawable);
            }
            @Override
            public void onError() {
                imageViewProfilePic.setImageResource(R.drawable.ic_launcher_foreground);
            }
        });

        textViewName.setText(user.getDisplayName());
        imageViewStarIcon.setImageResource(R.drawable.ic_grade_black_24dp);

        textViewImpact = (TextView) naviheaderview.findViewById(R.id.textViewImpactFactorVal);
        mFirebaseAuth = FirebaseAuth.getInstance();
        final String uid = mFirebaseAuth.getUid();
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFireBaseDatabase.getReference().child("users");
        mUsersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)) {
                    impact = Double.valueOf(dataSnapshot.child(uid).child("Impact").getValue().toString());
                    textViewImpact.setText(String.valueOf(impact));
                }
                else {
                    mUsersDatabaseReference.child(uid).setValue(new User(user.getDisplayName(),"0","50","50","0","0","0","50","50","50","50"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fab = (Button) findViewById(R.id.fab_scanbarcode);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                scanIntegrator.setOrientationLocked(false);
                scanIntegrator.initiateScan();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == IntentIntegrator.REQUEST_CODE) {
            //retrieve scan result
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            // if there actually is a result from the scannig activity
            if (scanningResult != null) {
                String scanContent = scanningResult.getContents(); //barcode number
                if(scanContent!= null){
                    //save result here, fragment transaction takes place in OnREsume
                    barcodeNumber = scanContent;
                }

            } else {
                Toast.makeText(getApplicationContext(), "No scan data received :(", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings) {

            setTitle("Shopping Cart");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new CartFragment();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        if (id == R.id.nav_news_feed) {
            // Handle the camera action
            fragment = new NewsFeedFragment();

        } else if (id ==R.id.nav_companies){
          fragment = new CompanyListFragment();

        } else if (id == R.id.nav_impact) {
            fragment = new ImpactFragment();

        } else if (id == R.id.nav_nearbyshops) {
            fragment = new GetNearbyShopsFragment();

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        if(fragment!=null){
            transaction.replace(R.id.fragment_container, fragment);
            getSupportFragmentManager().popBackStack();
            transaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onResume(){
        super.onResume();

        if (barcodeNumber!=null){
            Fragment pdtBizFrag = new ProductBusinessFragment();
            Bundle bundle = new Bundle ();
            bundle.putString("barcode num",barcodeNumber);
            bundle.putInt("mode",1);
            setTitle("Product details");
            pdtBizFrag.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container,pdtBizFrag);
            transaction.commit();
        }

    }
}
