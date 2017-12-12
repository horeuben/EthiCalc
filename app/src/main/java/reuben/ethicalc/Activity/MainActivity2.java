package reuben.ethicalc.Activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import reuben.ethicalc.Database.Company;
import reuben.ethicalc.Database.Product;
import reuben.ethicalc.Database.User;
import reuben.ethicalc.R;


public class MainActivity2 extends AppCompatActivity  {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ShareDialog shareDialog;
    private Button fbshare, logoutButton,testButton, dbButton;
    private TextView sharedContent;

    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mShopsDatabaseReference;
    private DatabaseReference mCompaniesDatabaseReference;
    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mProductsDatabaseReference;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mShopsDatabaseReference = mFireBaseDatabase.getReference().child("shops");
        mCompaniesDatabaseReference = mFireBaseDatabase.getReference().child("companies");
        mUsersDatabaseReference = mFireBaseDatabase.getReference().child("users");
        mProductsDatabaseReference = mFireBaseDatabase.getReference().child("products");

        mFirebaseAuth = FirebaseAuth.getInstance();
        final String uid = mFirebaseAuth.getUid();

        shareDialog = new ShareDialog(this);
        sharedContent = (TextView) findViewById(R.id.meme);
        fbshare = (Button) findViewById(R.id.button_fbshare);

        logoutButton = (Button) findViewById(R.id.button_logout);
        testButton = (Button) findViewById(R.id.button_test);
        dbButton = (Button) findViewById(R.id.button_dbutil);

        fbshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedContent.setBackground(getResources().getDrawable(R.drawable.share_impact));
                sharedContent.setText("52");
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    Bitmap image = getBitmapFromView(sharedContent);
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(image)
                            .setCaption("Hey how are you?")
                            .build();
                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                    shareDialog.show(content);
//                    if (ShareDialog.canShow(ShareLinkContent.class)) {
//                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                                .setContentTitle("Using EthiCalc!")
//                                //     .setImageUrl(Uri.parse("okLoginButton-min-300x136.png"))
//                                .setContentDescription("Hey whats up?")
//                                .setContentUrl(Uri.parse("www.google.com"))
//
//                                .build();
//                        shareDialog.show(linkContent);  // Show facebook ShareDialog
//                    }
                } else {
                    Toast.makeText(MainActivity2.this, "You need facebook installed!", Toast.LENGTH_LONG).show();
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity2.this, LoginActivity.class));
                finish();
            }
        });
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity2.this, MainActivity.class));

            }
        });

        dbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mUsersDatabaseReference.child(uid).setValue(new User("Cyrus Wang","500.32","56.21","53.42","2.79","28012.34","5341.32","48.12","50.23","50.01","52.21"));
//                mCompaniesDatabaseReference.push().setValue(new Company("ASICS Corporation", "Apparel", "54", "62", "50", "50", "50", "http://www.asics.com/medias2/ASICS-True-Blue-Profile-image.jpg?context=bWFzdGVyfGltYWdlc3wxNDY2OHxpbWFnZS9qcGVnfGltYWdlcy9oNTEvaDA4Lzg5NTAwMjI2MzU1NTAuanBnfDM5MmM1Mzk2YmEyZTc4Mzk0OTg2OGUzNzU1OWRjOTU1ZDVkOWFhNjIxYzI4ZDk3ZmQ1ZmQ0NWMxZjcxZjhkZGY"));
//                mCompaniesDatabaseReference.push().setValue(new Company("McDonalds Corporation", "Food and Beverages", "63", "62", "56", "58", "57", "https://i.pinimg.com/236x/40/9d/3e/409d3e481150063abeb42708430e1a89--mcdonald.jpg"));
//                mCompaniesDatabaseReference.push().setValue(new Company("Ferrero International", "Food and Beverages", "69", "59", "71", "69", "52", "http://www.springpool.de/wp-content/uploads/2016/09/Logo_Ferrero-512x512.png"));
//                mCompaniesDatabaseReference.push().setValue(new Company("Nestle", "Food and Beverages", "73", "65", "56", "72", "62", "https://print24.com/de/blog/wp-content/uploads/2009/12/logo8.jpg"));
//                mCompaniesDatabaseReference.push().setValue(new Company("Fast Retailing Co, Ltd", "Apparel", "44", "52", "47", "44", "41", "https://www.fastretailing.com/employment/images/sns/logo_fr.gif"));
//                mProductsDatabaseReference.push().setValue(new Product("Nutella 350G Hazelnut-Cocoa Spread", "80177173", "5.90", "Ferrero International"));
//                mProductsDatabaseReference.push().setValue(new Product("Milo Activ Go 3 IN 1 18 stik pek", "9556001217233", "6.50", "Nestle"));
            }
        });
    }

    // we have a textview with a background as image, and we want to create a bitmap through it to share from facebook when using
    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view

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
