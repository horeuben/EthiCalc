package reuben.ethicalc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import reuben.ethicalc.Database.NewsItem;
import reuben.ethicalc.R;

/**
 * Created by Jing Yun on 10/12/2017.
 */

public class NewsAdapter extends ArrayAdapter<NewsItem> {
    public List<NewsItem> articleList;
    Context context;


    public NewsAdapter(Context context, List<NewsItem> newsItemList) {
        super(context,0, newsItemList);
        this.context=context;
        this.articleList = newsItemList;
    }

    private class ViewHolder{
        TextView articleName;
        ImageView articleImg;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder = null;
        NewsItem newsItem = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.news_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.articleImg = (ImageView) convertView.findViewById(R.id.articleImg);
            viewHolder.articleName = (TextView) convertView.findViewById(R.id.articleTitle);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.articleTitle, viewHolder.articleName);
            convertView.setTag(R.id.articleImg, viewHolder.articleImg);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.articleName.setText(newsItem.getNewsTitle());
        Picasso.with(context)
                .load(newsItem.getImageURL())
                .into(viewHolder.articleImg);

        return convertView;
    }


}


