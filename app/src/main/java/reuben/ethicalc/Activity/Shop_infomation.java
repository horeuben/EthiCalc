package reuben.ethicalc.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

import reuben.ethicalc.Database.Shop;
import reuben.ethicalc.R;

public class Shop_infomation extends AppCompatActivity {
    ArrayList<String> shopArray;
    TextView shopname;
    TextView shopdis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_infomation);
        shopArray = (ArrayList<String>) getIntent().getSerializableExtra("shop");
        shopname = (TextView)findViewById(R.id.shopname);
        shopdis = (TextView)findViewById(R.id.shopDiscription);
        shopname.setText(shopArray.get(0));
        shopdis.setText(shopArray.get(1));
    }

}
