package reuben.ethicalc.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import reuben.ethicalc.R;

public class ItemInfoActivity extends AppCompatActivity {

    static List<Information> informationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Added dummy to cart", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                String dummyString = "this is a dummy string";
                informationList.add(new Information(dummyString,dummyString));

            }
        });
    }

    public static List<Information> getList(){ return informationList;}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
