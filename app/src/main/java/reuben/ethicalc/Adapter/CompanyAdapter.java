package reuben.ethicalc.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import reuben.ethicalc.Database.Company;
import reuben.ethicalc.Database.NewsItem;
import reuben.ethicalc.Database.ShopClass;
import reuben.ethicalc.R;

/**
 * Created by Jing Yun on 12/12/2017.
 */

public class CompanyAdapter extends ArrayAdapter<Company> {

    public List<Company> companyList;
    Context context;
    ViewHolder viewHolder;


    public CompanyAdapter(Context context, List<Company> companies) {
        super(context,0, companies);
        this.context=context;
        this.companyList = companies;
    }

    private class ViewHolder{
        ImageView companyLogo;
        TextView companyName;
        TextView companyCSR;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        viewHolder = null;
        Company company = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cards_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.companyLogo = (ImageView) convertView.findViewById(R.id.companyImgViewLogo);
            viewHolder.companyName = (TextView) convertView.findViewById(R.id.companyTextVName);
            viewHolder.companyCSR = (TextView) convertView.findViewById(R.id.companyTextVCSR);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.companyImgViewLogo, viewHolder.companyLogo);
            convertView.setTag(R.id.companyTextVName, viewHolder.companyName);
            convertView.setTag(R.id.companyTextVCSR, viewHolder.companyCSR);


        } else {
            viewHolder = (CompanyAdapter.ViewHolder) convertView.getTag();
        }


        viewHolder.companyName.setText(company.getCompanyName());
        Picasso.with(context)
                .load(company.getPictureUrl())
                .into(viewHolder.companyLogo);

        viewHolder.companyCSR.setText(company.getCSRRating());

        return convertView;
    }

    public void update(List<Company> data){

        this.companyList = data;
    }

}
