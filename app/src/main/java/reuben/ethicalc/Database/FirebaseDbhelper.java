package reuben.ethicalc.Database;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by reube on 6/12/2017.
 */

public class FirebaseDbhelper {
    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mShopsDatabaseReference;
    private DatabaseReference mCompaniesDatabaseReference;
    private ArrayList<Shop> shops;
    private ArrayList<Company> companies;
    private Shop shop;
    private Company company;

    public FirebaseDbhelper(){
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mShopsDatabaseReference = mFireBaseDatabase.getReference().child("shops");
        mCompaniesDatabaseReference = mFireBaseDatabase.getReference().child("companies");

    }
    public ArrayList<Shop> getShops(){
        shops = new ArrayList<>();
        mShopsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.e("linwei",dataSnapshot.toString());
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Shop shop = data.getValue(Shop.class);
                    shops.add(shop);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return shops;
    }

    public Company getCompany(String barcode){
        Query shop_query = mCompaniesDatabaseReference.orderByChild("barcode").equalTo(barcode);
        shop_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data :dataSnapshot.getChildren()) {
                        company = data.getValue(Company.class);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return company;
    }


}
