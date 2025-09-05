package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by sp on 10/05/18.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class RegisterRequest {

    private String ROLE_ADMIN;

    private String expired;

    private String accessToken;

    private String remark;

    private String referralCode;

    private String currentLevel;

    private String socialProvider;

    private String socialAccessToken;

    private String password;

    private String referedBy;

    private String emailVerified;

    private String city;

    private String authority;

    private String pinCode;

    private String username;

    private String ageRangeId;

    private String mobileVerified;

    private String userId;

    private String name;

    private String notification;

    private String countryId;

    private String enabled;

    private String createdTime;

    private String modifiedTime;

    private String mediaSecret;

    private String totalWins;

    private String totalGamePlayed;

    private String[] userAuthorities;

    private String ROLE_USER;

    private String totalLoyalityPoints;

    private String serviceId;

    private String email;

    private String socialProviderUserId;

    private String socialAccessTokenSecret;

    private String locked;

    private String profilePicId;

    private String mobile;

    public String getROLE_ADMIN ()
    {
        return ROLE_ADMIN;
    }

    public void setROLE_ADMIN (String ROLE_ADMIN)
    {
        this.ROLE_ADMIN = ROLE_ADMIN;
    }

    public String getExpired ()
    {
        return expired;
    }

    public void setExpired (String expired)
    {
        this.expired = expired;
    }

    public String getAccessToken ()
    {
        return accessToken;
    }

    public void setAccessToken (String accessToken)
    {
        this.accessToken = accessToken;
    }

    public String getRemark ()
    {
        return remark;
    }

    public void setRemark (String remark)
    {
        this.remark = remark;
    }

    public String getReferralCode ()
    {
        return referralCode;
    }

    public void setReferralCode (String referralCode)
    {
        this.referralCode = referralCode;
    }

    public String getCurrentLevel ()
    {
        return currentLevel;
    }

    public void setCurrentLevel (String currentLevel)
    {
        this.currentLevel = currentLevel;
    }

    public String getSocialProvider ()
    {
        return socialProvider;
    }

    public void setSocialProvider (String socialProvider)
    {
        this.socialProvider = socialProvider;
    }

    public String getSocialAccessToken ()
    {
        return socialAccessToken;
    }

    public void setSocialAccessToken (String socialAccessToken)
    {
        this.socialAccessToken = socialAccessToken;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public String getReferedBy ()
    {
        return referedBy;
    }

    public void setReferedBy (String referedBy)
    {
        this.referedBy = referedBy;
    }

    public String getEmailVerified ()
    {
        return emailVerified;
    }

    public void setEmailVerified (String emailVerified)
    {
        this.emailVerified = emailVerified;
    }

    public String getCity ()
    {
        return city;
    }

    public void setCity (String city)
    {
        this.city = city;
    }

    public String getAuthority ()
    {
        return authority;
    }

    public void setAuthority (String authority)
    {
        this.authority = authority;
    }

    public String getPinCode ()
    {
        return pinCode;
    }

    public void setPinCode (String pinCode)
    {
        this.pinCode = pinCode;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getAgeRangeId ()
    {
        return ageRangeId;
    }

    public void setAgeRangeId (String ageRangeId)
    {
        this.ageRangeId = ageRangeId;
    }

    public String getMobileVerified ()
    {
        return mobileVerified;
    }

    public void setMobileVerified (String mobileVerified)
    {
        this.mobileVerified = mobileVerified;
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

    public String getNotification ()
    {
        return notification;
    }

    public void setNotification (String notification)
    {
        this.notification = notification;
    }

    public String getCountryId ()
    {
        return countryId;
    }

    public void setCountryId (String countryId)
    {
        this.countryId = countryId;
    }

    public String getEnabled ()
    {
        return enabled;
    }

    public void setEnabled (String enabled)
    {
        this.enabled = enabled;
    }

    public String getCreatedTime ()
    {
        return createdTime;
    }

    public void setCreatedTime (String createdTime)
    {
        this.createdTime = createdTime;
    }

    public String getModifiedTime ()
    {
        return modifiedTime;
    }

    public void setModifiedTime (String modifiedTime)
    {
        this.modifiedTime = modifiedTime;
    }

    public String getMediaSecret ()
    {
        return mediaSecret;
    }

    public void setMediaSecret (String mediaSecret)
    {
        this.mediaSecret = mediaSecret;
    }

    public String getTotalWins ()
    {
        return totalWins;
    }

    public void setTotalWins (String totalWins)
    {
        this.totalWins = totalWins;
    }

    public String getTotalGamePlayed ()
    {
        return totalGamePlayed;
    }

    public void setTotalGamePlayed (String totalGamePlayed)
    {
        this.totalGamePlayed = totalGamePlayed;
    }

    public String[] getUserAuthorities ()
    {
        return userAuthorities;
    }

    public void setUserAuthorities (String[] userAuthorities)
    {
        this.userAuthorities = userAuthorities;
    }

    public String getROLE_USER ()
    {
        return ROLE_USER;
    }

    public void setROLE_USER (String ROLE_USER)
    {
        this.ROLE_USER = ROLE_USER;
    }

    public String getTotalLoyalityPoints ()
    {
        return totalLoyalityPoints;
    }

    public void setTotalLoyalityPoints (String totalLoyalityPoints)
    {
        this.totalLoyalityPoints = totalLoyalityPoints;
    }

    public String getServiceId ()
    {
        return serviceId;
    }

    public void setServiceId (String serviceId)
    {
        this.serviceId = serviceId;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getSocialProviderUserId ()
    {
        return socialProviderUserId;
    }

    public void setSocialProviderUserId (String socialProviderUserId)
    {
        this.socialProviderUserId = socialProviderUserId;
    }

    public String getSocialAccessTokenSecret ()
    {
        return socialAccessTokenSecret;
    }

    public void setSocialAccessTokenSecret (String socialAccessTokenSecret)
    {
        this.socialAccessTokenSecret = socialAccessTokenSecret;
    }

    public String getLocked ()
    {
        return locked;
    }

    public void setLocked (String locked)
    {
        this.locked = locked;
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
        return "ClassPojo [ROLE_ADMIN = "+ROLE_ADMIN+", expired = "+expired+", accessToken = "+accessToken+", remark = "+remark+", referralCode = "+referralCode+", currentLevel = "+currentLevel+", socialProvider = "+socialProvider+", socialAccessToken = "+socialAccessToken+", password = "+password+", referedBy = "+referedBy+", emailVerified = "+emailVerified+", city = "+city+", authority = "+authority+", pinCode = "+pinCode+", username = "+username+", ageRangeId = "+ageRangeId+", mobileVerified = "+mobileVerified+", userId = "+userId+", name = "+name+", notification = "+notification+", countryId = "+countryId+", enabled = "+enabled+", createdTime = "+createdTime+", modifiedTime = "+modifiedTime+", mediaSecret = "+mediaSecret+", totalWins = "+totalWins+", totalGamePlayed = "+totalGamePlayed+", userAuthorities = "+userAuthorities+", ROLE_USER = "+ROLE_USER+", totalLoyalityPoints = "+totalLoyalityPoints+", serviceId = "+serviceId+", email = "+email+", socialProviderUserId = "+socialProviderUserId+", socialAccessTokenSecret = "+socialAccessTokenSecret+", locked = "+locked+", profilePicId = "+profilePicId+", mobile = "+mobile+"]";
    }
}
