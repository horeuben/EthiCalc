package reuben.ethicalc.Database;

/**
 * Created by linweili on 6/12/17.
 */

public class Company {
    private String companyName;
    private String companyType;
    private String CSRRating;
    private String environmentRating;
    private String communityRating;
    private String employeesRating;
    private String governanceRating;
    private String barcode;
    private String pictureUrl;
    private Company() {
    }

    public Company(String companyName, String companyType, String CSRRating, String environmentRating, String communityRating, String employeesRating, String governanceRating, String barcode, String pictureUrl) {
        this.companyName = companyName;
        this.companyType = companyType;
        this.CSRRating = CSRRating;
        this.environmentRating = environmentRating;
        this.communityRating = communityRating;
        this.employeesRating = employeesRating;
        this.governanceRating = governanceRating;
        this.barcode = barcode;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
