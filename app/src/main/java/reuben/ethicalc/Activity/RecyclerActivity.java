package reuben.ethicalc.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.all.All;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import reuben.ethicalc.Adapter.ShopAdapter;
import reuben.ethicalc.Database.FirebaseDbhelper;
import reuben.ethicalc.Database.Shop;
import reuben.ethicalc.Database.ShopClass;
import reuben.ethicalc.R;

import static java.util.Collections.sort;

public class RecyclerActivity extends AppCompatActivity {
    private final String TAG = "display mall info";
    ArrayList<ShopClass> ShopClasses = new ArrayList<>();
    private RecyclerView recyclerView;
    private ShopAdapter mshopAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity2.KEY);
        Log.i("In recycler Activity",message);
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
        //ArrayList<Shop> Allshops = new ArrayList<>();
        //FirebaseDbhelper fbdbhelper = new FirebaseDbhelper(FirebaseDatabase.getInstance("https://fir-ethicalc.firebaseio.com/"));
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance("https://fir-ethicalc.firebaseio.com/");
        DatabaseReference mShopsDatabaseReference = mFirebaseDatabase.getReference().child("shops");
        final ArrayList<Shop> Allshops = new ArrayList<>();
        mShopsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.e("linwei",dataSnapshot.toString());
                Log.i("checking",String.valueOf(dataSnapshot.getChildrenCount()));
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
                    String[] imageurl = new String[ShopClasses.size()];
                    for (int i =0;i<ShopClasses.size();i++) {
                        imageurl[i] = ShopClasses.get(i).getImageUrl();

                    }
                    //GetImageTask getImageTask = new GetImageTask();
                    //getImageTask.execute(imageurl);




                    //ArrayList<ShopClass> data = updateData(shopPic);
                    mshopAdapter.update(ShopClasses);
                    mshopAdapter.notifyDataSetChanged();
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //Allshops = fbdbhelper.getShops();
        /*Shop canteen = new Shop("canteen","description for canteen","goo.gl/hwWsso");
        canteen.setLat("1.341162");
        canteen.setLng("103.963064");
        Shop gomgom = new Shop("gomgom","description for gomgom","https://www.halaltag.com/images/place/613-place.jpg");
        gomgom.setLat("1.340754");
        gomgom.setLng("103.962554");
        Allshops.add(canteen);
        Allshops.add(gomgom);
        */


    }

    public ArrayList<ShopClass> updateData(Bitmap...bitmaps){
        int counter = 0;
        ArrayList<ShopClass> ShopClass1 = new ArrayList<>();
        while (counter < ShopClasses.size()){
            ShopClass temp = ShopClasses.get(counter);
            temp.updateBitmap(bitmaps[counter]);
            ShopClass1.add(temp);
            counter++;
        }
        return ShopClass1;

    }
    class GetImageTask extends AsyncTask<String, Void, Bitmap[]> {
    @Override
    protected Bitmap[] doInBackground(String... urls){
        final Bitmap[] shopPic = new Bitmap[urls.length]; //Create new array of bitmap with length depending on the url

        for (int i =0;i<urls.length;i++){

            //URL url = new URL(urls[i]);
            ImageView tempImageview = new ImageView(RecyclerActivity.this);
            Picasso.with(RecyclerActivity.this).load(urls[i]).into(tempImageview);
            Bitmap imageBitmap = ((BitmapDrawable) tempImageview.getDrawable()).getBitmap();
            shopPic[i]=imageBitmap;

            //HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setDoInput(true);
            //connection.connect();
            //InputStream input = connection.getInputStream();
            /*InputStream input = url.openStream();
            shopPic[i] = BitmapFactory.decodeStream(input);*/
            Log.i("get pic successfully", "the"+i);
        /*} catch (IOException e) {
            // Log exception
            e.printStackTrace();
            Log.i("cannot get pic","the"+i);
        }*/
        }

        return shopPic;
    }

    @Override
    protected void onPostExecute(Bitmap[] shopPic){
        Log.i("Raf","Post executing...");
//            Toast.makeText(RecyclerActivity.this,"I'm at postexecute", Toast.LENGTH_SHORT).show();
        ArrayList<ShopClass> data = updateData(shopPic);
        mshopAdapter.update(data);
        mshopAdapter.notifyDataSetChanged();
    }
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
        Log.i(TAG,"sorted");
    }
}
