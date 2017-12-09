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
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



import reuben.ethicalc.R;


public class MainActivity2 extends AppCompatActivity  {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ShareDialog shareDialog;
    private Button fbshare, logoutButton,testButton;
    private TextView sharedContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        shareDialog = new ShareDialog(this);
        sharedContent = (TextView) findViewById(R.id.meme);
        sharedContent.setText("My impact factor is ");
        sharedContent.setBackground(getResources().getDrawable(R.drawable.pic));
        fbshare = (Button) findViewById(R.id.button_fbshare);

        logoutButton = (Button) findViewById(R.id.button_logout);
        testButton = (Button) findViewById(R.id.button_test);

        fbshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
