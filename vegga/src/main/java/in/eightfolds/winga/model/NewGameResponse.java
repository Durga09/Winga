package in.eightfolds.winga.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewGameResponse implements Serializable {
	@JsonIgnore
	private int currentContentID;
	private Long categoryGameId;
	private Long cgsId;
	private Long userId;
    private Boolean survey;
	public ArrayList<ContentResponse> getAppcontents() {
		return Appcontents;
	}

	public void setAppcontents(ArrayList<ContentResponse> appcontents) {
		Appcontents = appcontents;
	}

	private ArrayList<ContentResponse> Appcontents;
	private CategoryGameSessionMessage gameSessionMessage;
	private CategoryUserGameSession categoryUserGameSession;
	private List<Map<String, ContentResponse>> contents;
	private boolean eligible;
	private Integer noOfGameForEligible;
	private int noOfGameWins;
	private int noOfGamePlayed;
	private String currentTime;
	private String nextGameSessionTime;
	private FormResponse form;
	private List<MetaFormFeildType> formTypes;
	private int googleAdMobMaxViewCount;
	private int googleAdMobViewCount;
	private int googleAdMobPerViewPoints;
	private int googleAdMobViewThreshold;

	public Long getCategoryGameId() {
		return categoryGameId;
	}

	public void setCategoryGameId(Long categoryGameId) {
		this.categoryGameId = categoryGameId;
	}

	public Long getCgsId() {
		return cgsId;
	}

	public void setCgsId(Long cgsId) {
		this.cgsId = cgsId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public CategoryGameSessionMessage getGameSessionMessage() {
		return gameSessionMessage;
	}

	public void setGameSessionMessage(CategoryGameSessionMessage gameSessionMessage) {
		this.gameSessionMessage = gameSessionMessage;
	}

	public CategoryUserGameSession getCategoryUserGameSession() {
		return categoryUserGameSession;
	}

	public void setCategoryUserGameSession(CategoryUserGameSession categoryUserGameSession) {
		this.categoryUserGameSession = categoryUserGameSession;
	}

	public List<Map<String, ContentResponse>> getContents() {
		return contents;
	}

	public void setContents(List<Map<String, ContentResponse>> contents) {
		this.contents = contents;
	}

	public boolean isEligible() {
		return eligible;
	}

	public void setEligible(boolean eligible) {
		this.eligible = eligible;
	}

	public Integer getNoOfGameForEligible() {
		return noOfGameForEligible;
	}

	public void setNoOfGameForEligible(Integer noOfGameForEligible) {
		this.noOfGameForEligible = noOfGameForEligible;
	}

	public Integer getNoOfGameWins() {
		return noOfGameWins;
	}

	public void setNoOfGameWins(int noOfGameWins) {
		this.noOfGameWins = noOfGameWins;
	}

	public int getNoOfGamePlayed() {
		return noOfGamePlayed;
	}

	public void setNoOfGamePlayed(int noOfGamePlayed) {
		this.noOfGamePlayed = noOfGamePlayed;
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

	public FormResponse getForm() {
		return form;
	}

	public void setForm(FormResponse form) {
		this.form = form;
	}

	public List<MetaFormFeildType> getFormTypes() {
		return formTypes;
	}

	public void setFormTypes(List<MetaFormFeildType> formTypes) {
		this.formTypes = formTypes;
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

	public int getCurrentContentID() {
		return currentContentID;
	}

	public void setCurrentContentID(int currentContentID) {
		this.currentContentID = currentContentID;
	}

	public Boolean getSurvey() {
		return survey;
	}

	public void setSurvey(Boolean survey) {
		this.survey = survey;
	}
}
