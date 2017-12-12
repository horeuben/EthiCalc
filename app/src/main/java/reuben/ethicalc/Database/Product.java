package reuben.ethicalc.Database;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by linweili on 11/12/17.
 */

public class Product implements Parcelable{
    private String productName;
    private String barcode;
    private String MSRP;
    private String companyName;
    private String CSRRating;

    public Product(Parcel in){
        String[] data = new String[4];
        in.readStringArray(data);
        this.productName = data[0];
        this.barcode = data[1];
        this.MSRP = data[2];
        this.companyName = data[3];
        this.CSRRating = data[4];
    }
    public Product(){}
    public Product(String productName, String barcode, String MSRP, String companyName, String CSRRating) {
        this.productName = productName;
        this.barcode = barcode;
        this.MSRP = MSRP;
        this.companyName = companyName;
        this.CSRRating = CSRRating;
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

    public String getCSRRating() {
        return CSRRating;
    }

    public void setCSRRating(String CSRRating) {
        this.CSRRating = CSRRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {this.productName,
                this.barcode,
                this.MSRP,
                this.companyName});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
