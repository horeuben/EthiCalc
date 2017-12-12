package reuben.ethicalc.Database;

import android.graphics.Bitmap;

/**
 * Created by trying on 7/12/2017.
 */

public class ShopClass {

    private String name;
    private String description;

    double distance;
    public ShopClass(String name,String description,double distance){
        this.name = name;
        this.description = description;
        this.distance = distance;
    }
    public double getDistance() {return distance;}
    public String getName() {
        return name;
    }
    public String getDescription(){
        return description;
    }

}

