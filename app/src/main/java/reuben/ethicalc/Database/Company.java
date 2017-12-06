package reuben.ethicalc.Database;

/**
 * Created by reube on 6/12/2017.
 */

public class Company {
    public String companyName;
    public String barcode;
    public String description;
    public String pictureUrl;
    public Company() {
    }
    public Company(String companyName, String description, String pictureUrl, String barcode) {
        this.companyName = companyName;
        this.barcode = barcode;
        this.description = description;
        this.pictureUrl = pictureUrl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getDescription() {
        return description;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

}
