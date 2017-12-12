package reuben.ethicalc.Database;

/**
 * Created by linweili on 11/12/17.
 */

public class Product {
    public String productName;
    public String barcode;
    public String MSRP;
    public String companyName;

    public Product(String productName, String barcode, String MSRP, String companyName) {
        this.productName = productName;
        this.barcode = barcode;
        this.MSRP = MSRP;
        this.companyName = companyName;
    }
}
