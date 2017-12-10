package reuben.ethicalc.Database;

/**
 * Created by Jing Yun on 10/12/2017.
 */

public class NewsItem {

    private String newsTitle;
    private String imageURL;
    private String newsLink;

    public String getNewsTitle(){
        return newsTitle;
    }

    public String getImageURL(){
        return imageURL;
    }

    public String getNewsLink(){
        return newsLink;
    }

    public void setNewsTitle(String newTitle){
        this.newsTitle=newTitle;
    }

    public void setImageURL(String imgURL){
        this.imageURL=imgURL;
    }
    public void setNewsLink(String newsLink){
        this.newsLink = newsLink;
    }

    public NewsItem(String title, String imagelink, String articleLink){
        this.newsTitle = title;
        this.imageURL = imagelink;
        this.newsLink = articleLink;
    }

}

