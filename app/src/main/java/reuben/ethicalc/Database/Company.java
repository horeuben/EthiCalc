package reuben.ethicalc.Database;

/**
 * Created by reube on 6/12/2017.
 */

public class Company {
    private String companyName;
    private String barcode;
    private String description;
    private String pictureUrl;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Company() {
    }
    public Company(String companyName, String description, String pictureUrl, String barcode) {
        this.companyName = companyName;
        this.barcode = barcode;
        this.description = description;
        this.pictureUrl = pictureUrl;
    }


}
