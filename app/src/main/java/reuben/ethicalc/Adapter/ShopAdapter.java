package reuben.ethicalc.Adapter;

/**
 * Created by trying on 6/12/2017.
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import reuben.ethicalc.Database.ShopClass;
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
        viewHolder.shopName.setText(shopItem.getName());
        viewHolder.distanceName.setText(Math.round(shopItem.getDistance())+"m");
        return convertView;
    }

}
