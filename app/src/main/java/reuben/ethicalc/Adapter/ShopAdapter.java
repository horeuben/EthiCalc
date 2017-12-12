package reuben.ethicalc.Adapter;

/**
 * Created by trying on 6/12/2017.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;


import java.util.ArrayList;
import java.util.List;

import reuben.ethicalc.Database.Company;
import reuben.ethicalc.Database.ShopClass;
import reuben.ethicalc.Fragment.ProductBusinessFragment;
import reuben.ethicalc.R;
public class ShopAdapter extends ArrayAdapter<ShopClass> {
    public List<ShopClass> shopList;
    Context context;


    public ShopAdapter(Context context, List<ShopClass> shopItemList) {
        super(context,0, shopItemList);
        this.context=context;
        this.shopList = shopItemList;
    }
    public void update(List<ShopClass> data){
        this.shopList = data;
    }

    private class ViewHolder{
        TextView shopName;
        TextView distanceName;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder = null;
        ShopClass shopItem = getItem(position);
        Log.i("inside getView","shopItem"+shopItem);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cards_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.shopName = (TextView) convertView.findViewById(R.id.text_view_shop);
            viewHolder.distanceName = (TextView) convertView.findViewById(R.id.text_view_shop_distance);
            convertView.setTag(viewHolder);

            convertView.setTag(R.id.text_view_shop, viewHolder.shopName);
            convertView.setTag(R.id.text_view_shop_distance, viewHolder.distanceName);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.i("test","shopItem"+shopItem.getName());
        viewHolder.shopName.setText(shopItem.getName());
        viewHolder.distanceName.setText(Math.round(shopItem.getDistance())+"m");
        return convertView;
    }


}
/*
public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.shopViewHolder> {
    private ArrayList<ShopClass> data;
    private static int viewHolderCount = 0;
    Context parentContext;
    //private OnRecyclerViewItemClickListener mClickListener;
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
            {

        TextView shopTextView;
        TextView distanceTextView;
        View v;
        private FirebaseDatabase mFireBaseDatabase;
        private DatabaseReference mCompaniesDatabaseReference;

        shopViewHolder(View v){
            super(v);
            this.v = v;
            //v.setOnClickListener(this);
        }
        public void bind(int position){
            shopTextView = (TextView) this.v.findViewById(R.id.text_view_shop);
            distanceTextView = (TextView)this.v.findViewById(R.id.text_view_shop_distance);
            String shopName = data.get(position).getName();
            double dist = data.get(position).getDistance();
            distanceTextView.setText(Math.round(dist)+"m");
            shopTextView.setText(shopName);

        }


/*
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            ShopClass thisshop = data.get(clickedPosition);
            final String companyname = thisshop.getDescription();
            mFireBaseDatabase = FirebaseDatabase.getInstance("https://fir-ethicalc.firebaseio.com/");
            mCompaniesDatabaseReference = mFireBaseDatabase.getReference().child("companies");
            final Query companyQuery = mCompaniesDatabaseReference.orderByChild("companyName");
            companyQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot data: dataSnapshot.getChildren()){
                            Company company = data.getValue(Company.class);
                            if (company.getCompanyName()==companyname){
                                Fragment fragment = new ProductBusinessFragment();
                                Bundle bundle = new Bundle ();
                                bundle.putParcelable("company",company);
                                bundle.putInt("mode",0);
                                fragment.setArguments(bundle);

                                FragmentTransaction transaction = parentContext.getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container, fragment);
                                transaction.commit();
                            }
                        }
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }*/
