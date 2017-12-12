package reuben.ethicalc.Adapter;

/**
 * Created by trying on 6/12/2017.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import reuben.ethicalc.Database.ShopClass;
import reuben.ethicalc.R;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.shopViewHolder> {
    private ArrayList<ShopClass> data;
    private static int viewHolderCount = 0;
    Context parentContext;

    public ShopAdapter(Context context, ArrayList<ShopClass> data){
        this.parentContext = context;
        this.data = data;
    }

    @Override
    public shopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflates the viewholder layout, instantiate the VH class
        int layoutIDForListItem = R.layout.cards_layout;
        LayoutInflater inflater = LayoutInflater.from(parentContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIDForListItem,parent,shouldAttachToParentImmediately);

        shopViewHolder shopViewHolder = new shopViewHolder(view);

        return shopViewHolder;
    }

    @Override
    public void onBindViewHolder(shopViewHolder holder, int position) {
        //Attach data to the widget
        holder.bind(position);
        //Download image from url
    }

    @Override
    public int getItemCount(){
        //Return the number of items
        return data.size();
    }

    public void update(ArrayList<ShopClass> data){

        this.data = data;
    }


    class shopViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        ImageView shopImageView;
        TextView shopTextView;
        View v;

        shopViewHolder(View v){
            super(v);
            this.v = v;
            v.setOnClickListener(this);
        }
        public void bind(int position){
            shopTextView = (TextView) this.v.findViewById(R.id.text_view_shop);
            shopImageView = (ImageView) this.v.findViewById(R.id.image_view_shop);

            Picasso.with(parentContext).load(data.get(position).getImageUrl()).fit().into(shopImageView);

            String shopName = data.get(position).getName();
            double dist = data.get(position).getDistance();

            shopTextView.setText(shopName+"     "+Math.round(dist)+"m");

        }



        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            ShopClass thisshop = data.get(clickedPosition);
            String companyname = thisshop.getDescription();
            //Fragment

        }
    }


}