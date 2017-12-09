package reuben.ethicalc.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import reuben.ethicalc.R;

public class MainActivity2 extends AppCompatActivity{
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ShareDialog shareDialog;
    private Button fbButton, logoutButton,testButton;
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        shareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();
        fbButton = (Button) findViewById(R.id.button_fb);
        logoutButton = (Button) findViewById(R.id.button_logout);
        testButton = (Button) findViewById(R.id.button_test);
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Using EthiCalc!")
                            //     .setImageUrl(Uri.parse("okLoginButton-min-300x136.png"))
                            .setContentDescription("Hey whats up?")
                            .setContentUrl(Uri.parse("www.google.com"))

                            .build();
                    shareDialog.show(linkContent);  // Show facebook ShareDialog
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
}
