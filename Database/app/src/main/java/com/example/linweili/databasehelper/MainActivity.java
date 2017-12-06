package com.example.linweili.databasehelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mShopsDatabaseReference;
    private DatabaseReference mCompaniesDatabaseReference;
    private ChildEventListener mChildEventListener;


    private EditText shopname;
    private EditText description;
    private EditText pictureurl;
    private Button submit;

    private EditText comapnyname;
    private EditText description2;
    private EditText pictureurl2;
    private EditText barcode;
    private Button submit2;

    private Button queryshop_btn;
    private Button querycompany_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mShopsDatabaseReference = mFireBaseDatabase.getReference().child("shops");
        mCompaniesDatabaseReference = mFireBaseDatabase.getReference().child("companies");
        shopname = (EditText) findViewById(R.id.shopname);
        pictureurl = (EditText) findViewById(R.id.pictureurl);
        description = (EditText) findViewById(R.id.description);
        submit = (Button)  findViewById(R.id.submit);
        comapnyname = (EditText) findViewById(R.id.companyname);
        pictureurl2 = (EditText) findViewById(R.id.pictureurl2);
        description2 = (EditText) findViewById(R.id.description2);
        barcode = (EditText) findViewById(R.id.barcode);
        submit2 = (Button) findViewById(R.id.submit2);
        queryshop_btn = (Button) findViewById(R.id.queryshop);
        querycompany_btn = (Button) findViewById(R.id.querycompany);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shop shop = new Shop(shopname.getText().toString(), description.getText().toString(), pictureurl.getText().toString());
                mShopsDatabaseReference.push().setValue(shop);
            }
        });

        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Company company = new Company(comapnyname.getText().toString(), description2.getText().toString()
                ,pictureurl2.getText().toString(), barcode.getText().toString());
                mCompaniesDatabaseReference.push().setValue(company);
            }
        });



        //this is how you retrive data from shops
        queryshop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query shop_query = mShopsDatabaseReference.orderByChild("shopname").equalTo("Gom");
                shop_query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot data :dataSnapshot.getChildren()) {
                                Shop shop = data.getValue(Shop.class);
                                Log.e("linwei",shop.description);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


        //this is how you retrive data from companies
        querycompany_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query shop_query = mCompaniesDatabaseReference.orderByChild("barcode").equalTo("10");
                shop_query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot data :dataSnapshot.getChildren()) {
                                Company company = data.getValue(Company.class);
                                Log.e("linwei",company.description);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });



    }
}
