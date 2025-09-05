package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryResponse implements Serializable {

	private List<CategoryTranslation> categories;
	private User userDetail;
	private UserAddress primaryAddress;
	private UserRedemptionAmounts redemptionAmounts;
	private Notification notification;
	private List<CategoryGameSession> upcomingSponsoredGameSessions;
	private CategoryUserGameSession categoryUserGameSession;
	private CategoryGameSessionPrize categoryGameSessionPrize;
	private CategoryGameSession nextCategoryGameSession;
	private int googleAdMobMaxViewCount;
	private int googleAdMobViewCount;
	private int googleAdMobPerViewPoints;
	private int googleAdMobViewThreshold;
	private String currentTime;
	private int categoryType;
	private ExclusiveCategory exclusiveCategory;

	public List<CategoryTranslation> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryTranslation> categories) {
		this.categories = categories;
	}

	public User getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(User userDetail) {
		this.userDetail = userDetail;
	}

	public UserAddress getPrimaryAddress() {
		return primaryAddress;
	}

	public void setPrimaryAddress(UserAddress primaryAddress) {
		this.primaryAddress = primaryAddress;
	}

	public UserRedemptionAmounts getRedemptionAmounts() {
		return redemptionAmounts;
	}

	public void setRedemptionAmounts(UserRedemptionAmounts redemptionAmounts) {
		this.redemptionAmounts = redemptionAmounts;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	public List<CategoryGameSession> getUpcomingSponsoredGameSessions() {
		return upcomingSponsoredGameSessions;
	}

	public void setUpcomingSponsoredGameSessions(List<CategoryGameSession> upcomingSponsoredGameSessions) {
		this.upcomingSponsoredGameSessions = upcomingSponsoredGameSessions;
	}

	public CategoryUserGameSession getCategoryUserGameSession() {
		return categoryUserGameSession;
	}

	public void setCategoryUserGameSession(CategoryUserGameSession categoryUserGameSession) {
		this.categoryUserGameSession = categoryUserGameSession;
	}

	public CategoryGameSessionPrize getCategoryGameSessionPrize() {
		return categoryGameSessionPrize;
	}

	public void setCategoryGameSessionPrize(CategoryGameSessionPrize categoryGameSessionPrize) {
		this.categoryGameSessionPrize = categoryGameSessionPrize;
	}

	public CategoryGameSession getNextCategoryGameSession() {
		return nextCategoryGameSession;
	}

	public void setNextCategoryGameSession(CategoryGameSession nextCategoryGameSession) {
		this.nextCategoryGameSession = nextCategoryGameSession;
	}

	public int getGoogleAdMobMaxViewCount() {
		return googleAdMobMaxViewCount;
	}

	public void setGoogleAdMobMaxViewCount(int googleAdMobMaxViewCount) {
		this.googleAdMobMaxViewCount = googleAdMobMaxViewCount;
	}

	public int getGoogleAdMobViewCount() {
		return googleAdMobViewCount;
	}

	public void setGoogleAdMobViewCount(int googleAdMobViewCount) {
		this.googleAdMobViewCount = googleAdMobViewCount;
	}

	public int getGoogleAdMobPerViewPoints() {
		return googleAdMobPerViewPoints;
	}

	public void setGoogleAdMobPerViewPoints(int googleAdMobPerViewPoints) {
		this.googleAdMobPerViewPoints = googleAdMobPerViewPoints;
	}

	public int getGoogleAdMobViewThreshold() {
		return googleAdMobViewThreshold;
	}

	public void setGoogleAdMobViewThreshold(int googleAdMobViewThreshold) {
		this.googleAdMobViewThreshold = googleAdMobViewThreshold;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public int getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(int categoryType) {
		this.categoryType = categoryType;
	}

	@Override
	public String toString() {
		return "HomePageResponse [categories=" + categories + ", userDetail=" + userDetail + ", primaryAddress="
				+ primaryAddress + ", redemptionAmounts=" + redemptionAmounts + ", notification=" + notification
				+ ", upcomingSponsoredGameSessions=" + upcomingSponsoredGameSessions + ", categoryUserGameSession="
				+ categoryUserGameSession + ", categoryGameSessionPrize=" + categoryGameSessionPrize
				+ ", nextCategoryGameSession=" + nextCategoryGameSession + ", googleAdMobMaxViewCount="
				+ googleAdMobMaxViewCount + ", googleAdMobViewCount=" + googleAdMobViewCount
				+ ", googleAdMobPerViewPoints=" + googleAdMobPerViewPoints + ", googleAdMobViewThreshold="
				+ googleAdMobViewThreshold + ", currentTime=" + currentTime + "]";
	}

	public ExclusiveCategory getExclusiveCategory() {
		return exclusiveCategory;
	}

	public void setExclusiveCategories(ExclusiveCategory exclusiveCategory) {
		this.exclusiveCategory = exclusiveCategory;
	}
}
