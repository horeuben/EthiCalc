package reuben.ethicalc.Database;

/**
 * Created by linweili on 7/12/17.
 */

public class User {
    public String username;
    public String Cost;
    public String Impact;
    public String ImpStart;
    public String Delta;
    public String Accum;
    public String AccumWeek;
    public String last4Week;
    public String last3Week;
    public String last2Week;
    public String last1Week;

    public User(String username, String cost, String impact, String impStart, String delta, String accum, String accumWeek, String last4Week, String last3Week, String last2Week, String last1Week) {
        this.username = username;
        Cost = cost;
        Impact = impact;
        ImpStart = impStart;
        Delta = delta;
        Accum = accum;
        AccumWeek = accumWeek;
        this.last4Week = last4Week;
        this.last3Week = last3Week;
        this.last2Week = last2Week;
        this.last1Week = last1Week;
    }

    public User(){
    }
}
