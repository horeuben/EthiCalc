package reuben.ethicalc.Database;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by linweili on 6/12/17.
 */

public class Company implements Parcelable{
    private String companyName;
    private String companyType;
    private String CSRRating;
    private String environmentRating;
    private String communityRating;
    private String employeesRating;
    private String governanceRating;
    private String pictureUrl;


    public Company() {
    }
    public Company(Parcel in){
        String[] data = new String[8];
        in.readStringArray(data);
        this.companyName = data[0];
        this.companyType = data[1];
        this.CSRRating = data[2];
        this.environmentRating = data[3];
        this.communityRating = data[4];
        this.employeesRating = data[5];
        this.governanceRating = data[6];
        this.pictureUrl = data[7];
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        public Company[] newArray(int size) {
            return new Company[size];
        }
    };
    public Company(String companyName, String companyType, String CSRRating, String environmentRating, String communityRating, String employeesRating, String governanceRating, String pictureUrl) {
        this.companyName = companyName;
        this.companyType = companyType;
        this.CSRRating = CSRRating;
        this.environmentRating = environmentRating;
        this.communityRating = communityRating;
        this.employeesRating = employeesRating;
        this.governanceRating = governanceRating;
        this.pictureUrl = pictureUrl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getCSRRating() {
        return CSRRating;
    }

    public void setCSRRating(String CSRRating) {
        this.CSRRating = CSRRating;
    }

    public String getEnvironmentRating() {
        return environmentRating;
    }

    public void setEnvironmentRating(String environmentRating) {
        this.environmentRating = environmentRating;
    }

    public String getCommunityRating() {
        return communityRating;
    }

    public void setCommunityRating(String communityRating) {
        this.communityRating = communityRating;
    }

    public String getEmployeesRating() {
        return employeesRating;
    }

    public void setEmployeesRating(String employeesRating) {
        this.employeesRating = employeesRating;
    }

    public String getGovernanceRating() {
        return governanceRating;
    }

    public void setGovernanceRating(String governanceRating) {
        this.governanceRating = governanceRating;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {this.companyName,
        this.companyType,
        this.CSRRating,
        this.environmentRating,
        this.communityRating,
        this.employeesRating,
        this.governanceRating,
        this.pictureUrl});
    }
}
