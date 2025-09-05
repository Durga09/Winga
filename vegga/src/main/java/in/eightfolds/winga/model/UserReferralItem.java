package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserReferralItem implements Serializable {
    private String email;

    private String earnedAmt;

    private String userId;

    private String name;

    private String profilePicId;

    private String mobile;

    public int getEarnedPoint() {
        return earnedPoint;
    }

    public void setEarnedPoint(int earnedPoint) {
        this.earnedPoint = earnedPoint;
    }

    private int earnedPoint;

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getEarnedAmt()
    {
        return earnedAmt;
    }

    public void setEarnedAmt(String earnedAmt)
    {
        this.earnedAmt = earnedAmt;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getProfilePicId ()
    {
        return profilePicId;
    }

    public void setProfilePicId (String profilePicId)
    {
        this.profilePicId = profilePicId;
    }

    public String getMobile ()
    {
        return mobile;
    }

    public void setMobile (String mobile)
    {
        this.mobile = mobile;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [email = "+email+", earnedAmt = "+ earnedAmt +", userId = "+userId+", name = "+name+", profilePicId = "+profilePicId+", mobile = "+mobile+"]";
    }
}
