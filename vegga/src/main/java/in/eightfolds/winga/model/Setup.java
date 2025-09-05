package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Setup implements Serializable{

	private List<Country> countries;
	private List<UserLevel> userLevels;
	private List<AgeRange> ageRanges;
	private List<SupportType> supportTypes;
	private List<Language> languages;
	private List<State> states;
	private List<AddressType> addressTypes;
	private List<HelpDesk> helpDesks;
	private Double firstGameWinAmt;
	private Double firstGameLostAmt;
	private Double rupesForEachPoint;
	private String appShareImage;
	private String appShareText;
	private String referralText;



	private long setupVersion;
	private Double bonusReferralAmt;
	private Double minimumRedemptionAmt;
	private HomePageAd homePageAd;

	protected boolean goLive;
	protected String goLiveMessage;
	protected String currentTime;
	protected String goLiveTime;

	private List<RedemptionOption> redemptionOptions;


	private HashMap<Integer, String> supportRequestStates;

	//private long serverVersion;
	//private boolean isForcefulUpdate;

    private Map<Integer,MobileAppVersion> latestMobileAppVersions;

    public Map<Integer, MobileAppVersion> getLatestMobileAppVersions() {
        return latestMobileAppVersions;
    }

    public void setLatestMobileAppVersions(Map<Integer, MobileAppVersion> latestMobileAppVersions) {
        this.latestMobileAppVersions = latestMobileAppVersions;
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

	public List<Country> getCountries() {
		return countries;
	}
	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}
	public List<UserLevel> getUserLevels() {
		return userLevels;
	}
	public void setUserLevels(List<UserLevel> userLevels) {
		this.userLevels = userLevels;
	}
	public List<AgeRange> getAgeRanges() {
		return ageRanges;
	}
	public void setAgeRanges(List<AgeRange> ageRanges) {
		this.ageRanges = ageRanges;
	}
	public List<SupportType> getSupportTypes() {
		return supportTypes;
	}
	public void setSupportTypes(List<SupportType> supportTypes) {
		this.supportTypes = supportTypes;
	}
	public List<Language> getLanguages() {
		return languages;
	}
	public void setLanguages(List<Language> languages) {
		this.languages = languages;
	}
	public List<State> getStates() {
		return states;
	}
	public void setStates(List<State> states) {
		this.states = states;
	}

	public List<AddressType> getAddressTypes() {
		return addressTypes;
	}

	public void setAddressTypes(List<AddressType> addressTypes) {
		this.addressTypes = addressTypes;
	}

	public Double getFirstGameWinAmt() {
		return firstGameWinAmt;
	}
	public void setFirstGameWinAmt(Double firstGameWinAmt) {
		this.firstGameWinAmt = firstGameWinAmt;
	}
	public Double getFirstGameLostAmt() {
		return firstGameLostAmt;
	}
	public void setFirstGameLostAmt(Double firstGameLostAmt) {
		this.firstGameLostAmt = firstGameLostAmt;
	}
	public Double getRupesForEachPoint() {
		return rupesForEachPoint;
	}
	public void setRupesForEachPoint(Double rupesForEachPoint) {
		this.rupesForEachPoint = rupesForEachPoint;
	}
	public long getSetupVersion() {
		return setupVersion;
	}
	public void setSetupVersion(long setupVersion) {
		this.setupVersion = setupVersion;
	}

	public String getAppShareImage() {
		return appShareImage;
	}

	public void setAppShareImage(String appShareImage) {
		this.appShareImage = appShareImage;
	}

	public String getAppShareText() {
		return appShareText;
	}

	public void setAppShareText(String appShareText) {
		this.appShareText = appShareText;
	}

	public List<HelpDesk> getHelpDesks() {
		return helpDesks;
	}

	public void setHelpDesks(List<HelpDesk> helpDesks) {
		this.helpDesks = helpDesks;
	}

	public Double getBonusReferralAmt() {
		return bonusReferralAmt;
	}

	public void setBonusReferralAmt(Double bonusReferralAmt) {
		this.bonusReferralAmt = bonusReferralAmt;
	}

	public Double getMinimumRedemptionAmt() {
		return minimumRedemptionAmt;
	}

	public void setMinimumRedemptionAmt(Double minimumRedemptionAmt) {
		this.minimumRedemptionAmt = minimumRedemptionAmt;
	}

	public HashMap<Integer, String> getSupportRequestStates() {
		return supportRequestStates;
	}

	public void setSupportRequestStates(HashMap<Integer, String> supportRequestStates) {
		this.supportRequestStates = supportRequestStates;
	}

	public HomePageAd getHomePageAd() {
		return homePageAd;
	}

	public void setHomePageAd(HomePageAd homePageAd) {
		this.homePageAd = homePageAd;
	}

	public List<RedemptionOption> getRedemptionOptions() {
		return redemptionOptions;
	}

	public void setRedemptionOptions(List<RedemptionOption> redemptionOptions) {
		this.redemptionOptions = redemptionOptions;
	}

	public String getReferralText() {
		return referralText;
	}

	public void setReferralText(String referralText) {
		this.referralText = referralText;
	}
}
