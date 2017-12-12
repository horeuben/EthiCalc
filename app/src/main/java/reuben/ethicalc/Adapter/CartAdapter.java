package reuben.ethicalc.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import reuben.ethicalc.Activity.MainActivity;
import reuben.ethicalc.Database.Company;
import reuben.ethicalc.Database.Product;
import reuben.ethicalc.R;

/**
 * Created by Jing Yun on 13/12/2017.
 */

public class CartAdapter extends ArrayAdapter<Product>{
    public List<Product> pdtInCartList;
    Context context;
    CartAdapter.ViewHolder viewHolder;


    public CartAdapter(Context context, List<Product> resource) {
        super(context,0, resource);
        this.context=context;
        this.pdtInCartList= resource;
    }

    private class ViewHolder{
        TextView pdtName;
        Button delPdt;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        viewHolder = null;
        final Product product = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cart_item, parent, false);
            viewHolder = new CartAdapter.ViewHolder();
            viewHolder.pdtName = (TextView) convertView.findViewById(R.id.cart_textview);
            viewHolder.delPdt = (Button) convertView.findViewById(R.id.cart_delete);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.cart_textview, viewHolder.pdtName);
        } else {
            viewHolder = (CartAdapter.ViewHolder) convertView.getTag();
        }


        viewHolder.pdtName.setText(product.getCompanyName());
        viewHolder.delPdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("test","clicked");
                pdtInCartList.remove(position);
                notifyDataSetChanged();
            }

        });
        return convertView;
    }

}


