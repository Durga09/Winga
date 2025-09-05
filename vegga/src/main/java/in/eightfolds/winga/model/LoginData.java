package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.List;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginData extends User implements Serializable {
    private String accessToken;
    private String mediaSecret;
    private String socialAccessToken;
    private String socialAccessTokenSecret;
    private String socialProvider;
    private String socialProviderUserId;
    private NewGameResponse newGameResponse;
    private String orgName;
    private String venueName;
    private String imei;
    private List<UserAuthority> userAuthorities;


    public LoginData() {
    }
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public String getMediaSecret() {
        return mediaSecret;
    }
    public void setMediaSecret(String mediaSecret) {
        this.mediaSecret = mediaSecret;
    }
    public String getSocialAccessToken() {
        return socialAccessToken;
    }
    public void setSocialAccessToken(String socialAccessToken) {
        this.socialAccessToken = socialAccessToken;
    }
    public String getSocialAccessTokenSecret() {
        return socialAccessTokenSecret;
    }
    public void setSocialAccessTokenSecret(String socialAccessTokenSecret) {
        this.socialAccessTokenSecret = socialAccessTokenSecret;
    }
    public String getSocialProvider() {
        return socialProvider;
    }
    public void setSocialProvider(String socialProvider) {
        this.socialProvider = socialProvider;
    }
    public String getSocialProviderUserId() {
        return socialProviderUserId;
    }
    public void setSocialProviderUserId(String socialProviderUserId) {
        this.socialProviderUserId = socialProviderUserId;
    }
    public List<UserAuthority> getUserAuthorities() {
        return userAuthorities;
    }
    public void setUserAuthorities(List<UserAuthority> userAuthorities) {
        this.userAuthorities = userAuthorities;
    }
    public NewGameResponse getNewGameResponse() {
        return newGameResponse;
    }
    public void setNewGameResponse(NewGameResponse newGameResponse) {
        this.newGameResponse = newGameResponse;
    }

    public String getOrgName() {
        return orgName;
    }
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
    public String getVenueName() {
        return venueName;
    }
    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @JsonIgnore
    public User getUser(){
        return new User(userId, id,serviceId, name, username, password,notification,remark,currentLevel, ageRangeId,gender, countryId,countryCode, stateId, districtId, talukId,city, pincode,  totalLoyalityPoints, redeemedLoyalityPoints,redeemableAmt ,redeemedAmt, totalGamePlayed,  totalWins , locked, enabled, expired, authority, email, mobile, profilePicId, referedBy, referralCode, emailVerified, mobileVerified, alert, promotion, updates, firstGamePlayed, createdTime, modifiedTime,rewardsCount, preferredStateId,totalRewardsAmt,minimumRedemptionPoint,redemptionAmounts,goLive,goLiveMessage,currentTime,goLiveTime);
    }
}