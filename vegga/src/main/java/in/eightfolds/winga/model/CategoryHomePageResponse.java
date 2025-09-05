package in.eightfolds.winga.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryHomePageResponse implements Serializable {


	private Long categoryId;

	private Long langId;
	private ArrayList<GameSession> upcomingSponsoredGameSessions;
	private GameSessionMessage gameSessionMessage;
	private String currentTime;
	private User userDetail;
	private String nextGameSessionTime;
	public long getPublishTimeRemainingMillis() {
		return publishTimeRemainingMillis;
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

	private int googleAdMobMaxViewCount;
	private int googleAdMobViewCount;
	private int googleAdMobPerViewPoints;
	private int googleAdMobViewThreshold;

	public void setPublishTimeRemainingMillis(long publishTimeRemainingMillis) {
		this.publishTimeRemainingMillis = publishTimeRemainingMillis;
	}

	public long getSpecialSessionStartInMillis() {
		return specialSessionStartInMillis;
	}

	public void setSpecialSessionStartInMillis(long specialSessionStartInMillis) {
		this.specialSessionStartInMillis = specialSessionStartInMillis;
	}

	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

	@JsonIgnore
	private long publishTimeRemainingMillis ;
	@JsonIgnore
	private long specialSessionStartInMillis;
	private CategoryGameSession categoryGameSession;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public CategoryHomePageResponse() {
	}

	public CategoryHomePageResponse(Long categoryId, Long langId, GameSessionMessage gameSessionMessage,User userDetail,CategoryGameSession categoryGameSession) {
		super();
		this.categoryId = categoryId;
		this.langId = langId;
		this.gameSessionMessage = gameSessionMessage;
		this.categoryGameSession=categoryGameSession;
		this.userDetail=userDetail;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getLangId() {
		return langId;
	}

	public void setLangId(Long langId) {
		this.langId = langId;
	}

	public GameSessionMessage getGameSessionMessage() {
		return gameSessionMessage;
	}

	public void setGameSessionMessage(GameSessionMessage gameSessionMessage) {
		this.gameSessionMessage = gameSessionMessage;
	}

	@Override
	public String toString() {
		return "CategoryHomePageResponse [categoryId=" + categoryId + ", langId=" + langId + ", gameSessionMessage="
				+ gameSessionMessage + "]";
	}


	public CategoryGameSession getCategoryGameSession() {
		return categoryGameSession;
	}

	public void setCategoryGameSession(CategoryGameSession categoryGameSession) {
		this.categoryGameSession = categoryGameSession;
	}

	public User getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(User userDetail) {
		this.userDetail = userDetail;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getNextGameSessionTime() {
		return nextGameSessionTime;
	}

	public void setNextGameSessionTime(String nextGameSessionTime) {
		this.nextGameSessionTime = nextGameSessionTime;
	}

	public ArrayList<GameSession> getUpcomingSponsoredGameSessions() {
		return upcomingSponsoredGameSessions;
	}

	public void setUpcomingSponsoredGameSessions(ArrayList<GameSession> upcomingSponsoredGameSessions) {
		this.upcomingSponsoredGameSessions = upcomingSponsoredGameSessions;
	}
}
