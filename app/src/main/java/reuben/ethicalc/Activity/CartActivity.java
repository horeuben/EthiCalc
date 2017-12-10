package reuben.ethicalc.Activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import reuben.ethicalc.R;

public class CartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerCart);
        rv.setLayoutManager(mLayoutManager);

        RVAdapter adapter = new RVAdapter(ItemInfoActivity.getList());

        rv.setAdapter(adapter);

        Button fab = (Button) findViewById(R.id.fab_addimpact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Go to impact", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent dummy = new Intent(getApplicationContext(), MainActivity.class);
                dummy.putExtra("fragment", "Impactfragment");
                startActivity(dummy);
            }
        });

    }

    public class RVAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<Information> dataSource;

        public RVAdapter(List<Information> dataArgs){
            dataSource = dataArgs;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.cart_row, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Information information = dataSource.get(position);
            holder.tips_ttle.setText(information.item_title);
            holder.tips_cons.setText(information.shop_title);
            holder.itemView.setTag(information);

        }

        @Override
        public int getItemCount() {
            return dataSource.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tips_ttle;
        public TextView tips_cons;

        public ViewHolder(View itemView) {
            super(itemView);
            tips_ttle = (TextView) itemView.findViewById(R.id.item_title);
            tips_cons = (TextView) itemView.findViewById(R.id.shop_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Information info = (Information) itemView.getTag();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

class Information {
    protected String item_title;
    protected String shop_title;

    Information(){};
    Information(String item, String shop){
        this.item_title = item;
        this.shop_title = shop;
    };
}
