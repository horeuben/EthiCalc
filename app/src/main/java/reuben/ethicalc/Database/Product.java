package reuben.ethicalc.Database;

/**
 * Created by linweili on 11/12/17.
 */

public class Product {
    private String productName;
    private String barcode;
    private String MSRP;
    private String companyName;

    public Product(String productName, String barcode, String MSRP, String companyName) {
        this.productName = productName;
        this.barcode = barcode;
        this.MSRP = MSRP;
        this.companyName = companyName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getMSRP() {
        return MSRP;
    }

    public void setMSRP(String MSRP) {
        this.MSRP = MSRP;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
