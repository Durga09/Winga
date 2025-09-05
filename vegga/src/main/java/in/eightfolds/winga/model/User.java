package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class  User implements Serializable {

	protected long userId;
	protected long id;
	protected Integer serviceId;
	protected String name;
	protected String username;
	protected String password;
	protected boolean notification;
	protected String remark;
	protected Long countryId;

	protected String countryCode;
	protected String city;
	protected String pincode;
	protected Double totalLoyalityPoints;
	protected Integer totalGamePlayed;
	protected Integer totalWins;
	protected Long currentLevel;
	protected Long ageRangeId;
	protected Long gender;
	protected boolean locked;
	protected boolean enabled;
	protected boolean expired;
	protected String authority;
	protected String email;
	protected String mobile;
	protected Long profilePicId;
	protected Long referedBy;
	protected String referralCode;
	protected boolean emailVerified = false;
	protected boolean mobileVerified = false;
	protected boolean alert;
	protected boolean promotion;
	protected boolean updates;
	protected boolean firstGamePlayed;
	protected String createdTime;
	protected String modifiedTime;
	protected UserAddress primaryAddress;

	protected Double redeemedLoyalityPoints;
	protected Double redeemableAmt;
	protected Double redeemedAmt;
	protected Double rewardsCount;
	protected float totalRewardsAmt;
	@JsonProperty("preferredStateId")
	protected Long preferredStateId;

	@JsonProperty("preferredContentType")
	protected int preferredContentType;

	protected Long stateId;
	protected Long talukId;
	protected Long districtId;
	protected Double minimumRedemptionPoint;
	protected RedemptionAmounts redemptionAmounts;
	protected boolean goLive;
	protected String goLiveMessage;
	protected String currentTime;
	protected String goLiveTime;

	public UserDeviceDetail getUserDeviceDetail() {
		return userDeviceDetail;
	}

	public void setUserDeviceDetail(UserDeviceDetail userDeviceDetail) {
		this.userDeviceDetail = userDeviceDetail;
	}

	private UserDeviceDetail userDeviceDetail;


	public User() {
	}

	public User(long userId,long id, Integer serviceId, String name, String username, String password, boolean notification, String remark, Long currentLevel, Long ageRangeId, Long gender, Long countryId, String countryCode, Long stateId, Long districtId, Long talukId, String city, String pincode, Double totalLoyalityPoints, Double redeemedLoyalityPoints, Double redeemableAmt, Double redeemedAmt, Integer totalGamePlayed, Integer totalWins, boolean locked, boolean enabled, boolean expired, String authority, String email, String mobile, Long profilePicId, Long referedBy, String referralCode, boolean emailVerified, boolean mobileVerified, boolean alert, boolean promotion, boolean updates, boolean firstGamePlayed, String createdTime, String modifiedTime, Double rewardsCount, Long preferredStateId, Float totalRewardsAmt, Double minimumRedemptionPoint, RedemptionAmounts redemptionAmounts, boolean goLive,String goLiveMessage, String currentTime, String goLiveTime) {
		this.userId = userId;
		this.id=id;
		this.serviceId = serviceId;
		this.currentLevel = currentLevel;
		this.ageRangeId = ageRangeId;
		this.gender = gender;
		this.name = name;
		this.username = username;
		this.password = password;
		this.notification = notification;
		this.remark = remark;
		this.countryId = countryId;
		this.countryCode = countryCode;
		this.stateId = stateId;
		this.districtId = districtId;
		this.talukId = talukId;
		this.city = city;
		this.pincode = pincode;
		this.totalLoyalityPoints = totalLoyalityPoints;
		this.redeemedLoyalityPoints = redeemedLoyalityPoints;
		this.redeemableAmt = redeemableAmt;
		this.redeemedAmt = redeemedAmt;
		this.totalGamePlayed = totalGamePlayed;
		this.totalWins = totalWins;
		this.locked = locked;
		this.enabled = enabled;
		this.expired = expired;
		this.authority = authority;
		this.email = email;
		this.mobile = mobile;
		this.profilePicId = profilePicId;
		this.referedBy = referedBy;
		this.referralCode = referralCode;
		this.emailVerified = emailVerified;
		this.mobileVerified = mobileVerified;
		this.alert = alert;
		this.promotion = promotion;
		this.updates = updates;
		this.firstGamePlayed = firstGamePlayed;
		this.createdTime = createdTime;
		this.modifiedTime = modifiedTime;
		this.rewardsCount = rewardsCount;
		this.preferredStateId = preferredStateId;
		this.totalRewardsAmt = totalRewardsAmt;
		this.minimumRedemptionPoint = minimumRedemptionPoint;
		this.redemptionAmounts = redemptionAmounts;
		this.goLive = goLive;
		this.goLiveMessage = goLiveMessage;
		this.currentTime = currentTime;
		this.goLiveTime = goLiveTime;
	}


	public boolean isGoLive() {
		return goLive;
	}

	public void setGoLive(boolean goLive) {
		this.goLive = goLive;
	}

	public String getGoLiveMessage() {
		return goLiveMessage;
	}

	public void setGoLiveMessage(String goLiveMessage) {
		this.goLiveMessage = goLiveMessage;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getGoLiveTime() {
		return goLiveTime;
	}

	public void setGoLiveTime(String goLiveTime) {
		this.goLiveTime = goLiveTime;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isNotification() {
		return notification;
	}

	public void setNotification(boolean notification) {
		this.notification = notification;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getCountryCode() {
		return countryCode;
	}


	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pinCode) {
		this.pincode = pinCode;
	}

	public Double getTotalLoyalityPoints() {
		return totalLoyalityPoints;
	}

	public void setTotalLoyalityPoints(Double totalLoyalityPoints) {
		this.totalLoyalityPoints = totalLoyalityPoints;
	}

	public Integer getTotalGamePlayed() {
		return totalGamePlayed;
	}

	public void setTotalGamePlayed(Integer totalGamePlayed) {
		this.totalGamePlayed = totalGamePlayed;
	}

	public Integer getTotalWins() {
		return totalWins;
	}

	public void setTotalWins(Integer totalWins) {
		this.totalWins = totalWins;
	}

	public Long getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(Long currentLevel) {
		this.currentLevel = currentLevel;
	}

	public Long getAgeRangeId() {
		return ageRangeId;
	}

	public void setAgeRangeId(Long ageRangeId) {
		this.ageRangeId = ageRangeId;
	}

	public Long getGender() {
		return gender;
	}

	public void setGender(Long gender) {
		this.gender = gender;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getProfilePicId() {
		return profilePicId;
	}

	public void setProfilePicId(Long profilePicId) {
		this.profilePicId = profilePicId;
	}

	public Long getReferedBy() {
		return referedBy;
	}

	public void setReferedBy(Long referedBy) {
		this.referedBy = referedBy;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public boolean isMobileVerified() {
		return mobileVerified;
	}

	public void setMobileVerified(boolean mobileVerified) {
		this.mobileVerified = mobileVerified;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}


	public boolean isAlert() {
		return alert;
	}

	public void setAlert(boolean alert) {
		this.alert = alert;
	}

	public boolean isPromotion() {
		return promotion;
	}

	public void setPromotion(boolean promotion) {
		this.promotion = promotion;
	}

	public boolean isUpdates() {
		return updates;
	}

	public void setUpdates(boolean updates) {
		this.updates = updates;
	}


	public boolean isFirstGamePlayed() {
		return firstGamePlayed;
	}

	public void setFirstGamePlayed(boolean firstGamePlayed) {
		this.firstGamePlayed = firstGamePlayed;
	}

	public UserAddress getPrimaryAddress() {
		return primaryAddress;
	}

	public void setPrimaryAddress(UserAddress primaryAddress) {
		this.primaryAddress = primaryAddress;
	}

	public Double getRedeemedLoyalityPoints() {
		return redeemedLoyalityPoints;
	}

	public void setRedeemedLoyalityPoints(Double redeemedLoyalityPoints) {
		this.redeemedLoyalityPoints = redeemedLoyalityPoints;
	}

	public Double getRedeemableAmt() {
		return redeemableAmt;
	}

	public void setRedeemableAmt(Double redeemableAmt) {
		this.redeemableAmt = redeemableAmt;
	}

	public Double getRedeemedAmt() {
		return redeemedAmt;
	}

	public void setRedeemedAmt(Double redeemedAmt) {
		this.redeemedAmt = redeemedAmt;
	}

	public Double getRewardsCount() {
		return rewardsCount;
	}

	public void setRewardsCount(Double rewardsCount) {
		this.rewardsCount = rewardsCount;
	}

	public Long getPreferredStateId() {
		return preferredStateId;
	}

	public void setPreferredStateId(Long preferredStateId) {
		this.preferredStateId = preferredStateId;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public float getTotalRewardsAmt() {
		return totalRewardsAmt;
	}

	public void setTotalRewardsAmt(float totalRewardsAmt) {
		this.totalRewardsAmt = totalRewardsAmt;
	}

	public Long getTalukId() {
		return talukId;
	}

	public void setTalukId(Long talukId) {
		this.talukId = talukId;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public Double getMinimumRedemptionPoint() {
		return minimumRedemptionPoint;
	}

	public void setMinimumRedemptionPoint(Double minimumRedemptionPoint) {
		this.minimumRedemptionPoint = minimumRedemptionPoint;
	}

	public RedemptionAmounts getRedemptionAmounts() {
		return redemptionAmounts;
	}

	public void setRedemptionAmounts(RedemptionAmounts redemptionAmounts) {
		this.redemptionAmounts = redemptionAmounts;
	}

	public int getPreferredContentType() {
		return preferredContentType;
	}

	public void setPreferredContentType(int preferredContentType) {
		this.preferredContentType = preferredContentType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
