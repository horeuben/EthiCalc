package reuben.ethicalc.Database;

/**
 * Created by linweili on 6/12/17.
 */

public class Company {
    public String companyName;
    public String companyType;
    public String CSRRating;
    public String environmentRating;
    public String communityRating;
    public String employeesRating;
    public String governanceRating;
    public String pictureUrl;
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
}
