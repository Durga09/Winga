package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameResultResponse implements Serializable {

	private List<ContentResponse> contents;
	private GameResultMessage gameResultMessage;
	private List<GameResult> gameResults;
	private boolean winFlag;
	private boolean eligible;
	private int noOfGameForEligible;
	private int noOfGameWins;
	private int noOfGamePlayed;
	private Double pointsWin;
	private Long scratchCardId;
	private String congratsMsg;
	private List<BuyLinkData> buyLinks;
	private List<SurveyResult> surveyResults;

	public boolean isSurvey() {
		return survey;
	}

	public void setSurvey(boolean survey) {
		this.survey = survey;
	}

	private boolean survey;

	public List<ContentResponse> getContents() {
		return contents;
	}

	public void setContents(List<ContentResponse> contents) {
		this.contents = contents;
	}

	public boolean isWinFlag() {
		return winFlag;
	}

	public void setWinFlag(boolean winFlag) {
		this.winFlag = winFlag;
	}

	public GameResultMessage getGameResultMessage() {
		return gameResultMessage;
	}

	public void setGameResultMessage(GameResultMessage gameResultMessage) {
		this.gameResultMessage = gameResultMessage;
	}

	public boolean isEligible() {
		return eligible;
	}

	public void setEligible(boolean eligible) {
		this.eligible = eligible;
	}

	public int getNoOfGameForEligible() {
		return noOfGameForEligible;
	}

	public void setNoOfGameForEligible(int noOfGameForEligible) {
		this.noOfGameForEligible = noOfGameForEligible;
	}

	public int getNoOfGameWins() {
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

	public Double getPointsWin() {
		return pointsWin;
	}

	public void setPointsWin(Double pointsWin) {
		this.pointsWin = pointsWin;
	}

	public Long getScratchCardId() {
		return scratchCardId;
	}

	public void setScratchCardId(Long scratchCardId) {
		this.scratchCardId = scratchCardId;
	}

	public String getCongratsMsg() {
		return congratsMsg;
	}

	public void setCongratsMsg(String congratsMsg) {
		this.congratsMsg = congratsMsg;
	}
    
	public List<GameResult> getGameResults() {
		return gameResults;
	}

	public void setGameResults(List<GameResult> gameResults) {
		this.gameResults = gameResults;
	}

	public List<BuyLinkData> getBuyLinks() {
		return buyLinks;
	}

	public void setBuyLinks(List<BuyLinkData> buyLinks) {
		this.buyLinks = buyLinks;
	}

	@Override
	public String toString() {
		return "GameResultResponse [contents=" + contents + ", gameResultMessage=" + gameResultMessage
				+ ", gameResults=" + gameResults + ", winFlag=" + winFlag + ", eligible=" + eligible
				+ ", noOfGameForEligible=" + noOfGameForEligible + ", noOfGameWins=" + noOfGameWins
				+ ", noOfGamePlayed=" + noOfGamePlayed + ", pointsWin=" + pointsWin + ", scratchCardId=" + scratchCardId
				+ ", congratsMsg=" + congratsMsg + ", buyLinks=" + buyLinks + "]";
	}

	public List<SurveyResult> getSurveyResults() {
		return surveyResults;
	}

	public void setSurveyResults(List<SurveyResult> surveyResults) {
		this.surveyResults = surveyResults;
	}
}
