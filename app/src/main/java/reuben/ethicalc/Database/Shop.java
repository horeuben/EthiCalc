package reuben.ethicalc.Database;

/**
 * Created by reube on 6/12/2017.
 */

public class Shop {
    private String shopname;
    private String description;
    private String picureurl;

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicureurl() {
        return picureurl;
    }

    public void setPicureurl(String picureurl) {
        this.picureurl = picureurl;
    }

    public Shop() {

    }
    public Shop (String shopname, String description, String picureurl) {
        this.shopname = shopname;
        this.description = description;
        this.picureurl = picureurl;
    }

}