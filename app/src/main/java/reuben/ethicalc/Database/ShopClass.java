package reuben.ethicalc.Database;

import android.graphics.Bitmap;

/**
 * Created by trying on 7/12/2017.
 */

public class ShopClass {

    private String name;
    private String description;
    private String imageurl ;
    private Bitmap imageBitmap;
    double distance;
    public ShopClass(String name,String description,String url,double distance){
        this.name = name;
        this.description = description;
        this.imageurl = url;
        this.distance = distance;
    }
    public double getDistance() {return distance;}
    public String getName() {
        return name;
    }
    public String getDescription(){
        return description;
    }
    public String getImageUrl(){
        return this.imageurl;
    }
    public Bitmap getImageBitmap(){
        return imageBitmap;
    }
    public void updateBitmap(Bitmap imageBitmap){
        this.imageBitmap = imageBitmap;
    }
}

