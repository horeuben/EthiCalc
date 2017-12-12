package reuben.ethicalc.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import reuben.ethicalc.Fragment.BlankFragment;
import reuben.ethicalc.Fragment.CompanyListFragment;
import reuben.ethicalc.Fragment.GetNearbyShopsFragment;
import reuben.ethicalc.Fragment.ImpactFragment;
import reuben.ethicalc.Fragment.NewsFeedFragment;
import reuben.ethicalc.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BlankFragment.OnFragmentInteractionListener,GetNearbyShopsFragment.OnFragmentInteractionListener,
        NewsFeedFragment.OnFragmentInteractionListener,ImpactFragment.OnFragmentInteractionListener,CompanyListFragment.OnFragmentInteractionListener {
    private FirebaseUser user;
    private ImageView imageViewProfilePic, imageViewStarIcon;

    private TextView textViewName;

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
        Button fab = (Button) findViewById(R.id.fab_scanbarcode);
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
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();
                if(scanContent!= null && scanFormat !=null){
                    //                    //scan content is the number on the barcode--> use this number to get information about copmany
//
//                    //get the company by searching on this website http://gepir.gs1.org/index.php/search-by-gtin
                }
                Toast.makeText(getApplicationContext(), "Hi"+scanContent+" "+scanFormat, Toast.LENGTH_SHORT).show();
//                if(scanContent!=null&&scanFormat!=null) {
//                    //different kinds of barcodes: EAN13, EAN_8, UPC_12

//                    Toast.makeText(getApplicationContext(), "Go to dummy info activity", Toast.LENGTH_SHORT).show();
//                    Intent dummy = new Intent(this, ItemInfoActivity.class);
//                    startActivity(dummy);
//                }//once get, throw it to another activity to display company info
//                else{
//                    Toast.makeText(getApplicationContext(), "No scan data received :(", Toast.LENGTH_SHORT).show();
//                }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "Go to cart activity", Toast.LENGTH_SHORT).show();
            Intent dummy = new Intent(this, CartActivity.class);
            startActivity(dummy);
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
        if (id == R.id.nav_home) {
            // Handle the camera action
            fragment = new NewsFeedFragment();

        } else if (id ==R.id.nav_database){
          fragment = new CompanyListFragment();

        } else if (id == R.id.nav_impact) {
            fragment = new ImpactFragment();

        } else if (id == R.id.nav_share) {
            fragment = new GetNearbyShopsFragment();

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        if(fragment!=null){
            transaction.replace(R.id.fragment_container,fragment);
            transaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //convert image into circle
    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    @Override
    public void onResume(){
        super.onResume();

        Intent intent = getIntent();
        String fragment = "";
        if (intent.getExtras() != null){
            fragment = intent.getExtras().getString("fragment");
            intent.removeExtra("fragment");
        }

        if(intent !=null) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            switch(fragment){

                default:
                    //change to different fragments
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BlankFragment()).commit();
                    navigationView.getMenu().getItem(0).setChecked(true);
                    onNavigationItemSelected(navigationView.getMenu().getItem(0));
                    break;
                case "Impactfragment":
                    //go to impact fragment;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ImpactFragment()).commit();
                    navigationView.getMenu().getItem(3).setChecked(true);
                    onNavigationItemSelected(navigationView.getMenu().getItem(3));
                    break;
            }
        }

    }
}
