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
    private String pictureUrl;
    public Company() {
    }

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

    public String getCompanyName(){
        return this.companyName;
    }

    public String getCSRRating(){
        return this.CSRRating;
    }

    public String getPictureUrl(){
        return this.pictureUrl;
    }
}
