package reuben.ethicalc.Activity;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import reuben.ethicalc.Adapter.ShopAdapter;
import reuben.ethicalc.Database.Shop;
import reuben.ethicalc.Database.ShopClass;
import reuben.ethicalc.R;

import static java.util.Collections.sort;

public class RecyclerActivity extends AppCompatActivity {

    ArrayList<ShopClass> ShopClasses = new ArrayList<>();
    private RecyclerView recyclerView;
    private ShopAdapter mshopAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity2.KEY);

        String[] separated = message.split(":");
        recyclerView = findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecyclerActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mshopAdapter = new ShopAdapter(RecyclerActivity.this, ShopClasses);
        recyclerView.setAdapter(mshopAdapter);

        double lat = Double.valueOf(separated[0]);
        double lon = Double.valueOf(separated[1]);
        final Location mylocation = new Location("me");
        mylocation.setLatitude(lat);
        mylocation.setLongitude(lon);

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance("https://fir-ethicalc.firebaseio.com/");
        DatabaseReference mShopsDatabaseReference = mFirebaseDatabase.getReference().child("shops");
        final ArrayList<Shop> Allshops = new ArrayList<>();
        mShopsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Shop shop = data.getValue(Shop.class);
                    Allshops.add(shop);
                }

                for (Shop thisshop : Allshops){
                    String Locationname = thisshop.getShopname();
                    Location newloc = new Location("check");
                    newloc.setLongitude(Double.valueOf(thisshop.getLng()));
                    newloc.setLatitude(Double.valueOf(thisshop.getLat()));
                    double distance = mylocation.distanceTo(newloc);
                    if (distance<200) {
                        ShopClass newshop = new ShopClass(Locationname,thisshop.getDescription(),thisshop.getPicureurl(),distance);
                        ShopClasses.add(newshop);

                    }}
                    sort(ShopClasses);

                    mshopAdapter.update(ShopClasses);
                    mshopAdapter.notifyDataSetChanged();
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void sort(ArrayList<ShopClass> shops ){
        int n = shops.size();
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (shops.get(j).getDistance() > shops.get(j + 1).getDistance()) {
                    ShopClass temp = shops.get(j);
                    shops.set(j, shops.get(j + 1));
                    shops.set(j + 1, temp);
                }
            }
        }
    }
}
