package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import in.eightfolds.utils.DateTime;

/**
 * Created by sp on 14/05/18.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameSession implements Serializable {

    private Long gSessionId;
    private String startTime;
    private String endTime;
    private Integer noOfContentsEachGame;
    private Integer noOfQuestionsEachContent;
    private Integer noOfOptionsEachQuestion;
    private Integer noOfGameWinForEligibility;
    private Double pointsForEachGameWin;
    private Double pointsForEachGameLost;
    private Integer state;
    private Double totalRevenue;
    private Integer totalWiners;
    private String winerPubTime;
    private boolean deleted;
    private String createdTime;
    private String modifiedTime;
    private String currentTime;

    private boolean paused;
    private boolean sponsored;


    public Long getgSessionId() {
        return gSessionId;
    }
    public void setgSessionId(Long gSessionId) {
        this.gSessionId = gSessionId;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public Integer getNoOfContentsEachGame() {
        return noOfContentsEachGame;
    }
    public void setNoOfContentsEachGame(Integer noOfContentsEachGame) {
        this.noOfContentsEachGame = noOfContentsEachGame;
    }
    public Integer getNoOfQuestionsEachContent() {
        return noOfQuestionsEachContent;
    }
    public void setNoOfQuestionsEachContent(Integer noOfQuestionsEachContent) {
        this.noOfQuestionsEachContent = noOfQuestionsEachContent;
    }
    public Integer getNoOfOptionsEachQuestion() {
        return noOfOptionsEachQuestion;
    }
    public void setNoOfOptionsEachQuestion(Integer noOfOptionsEachQuestion) {
        this.noOfOptionsEachQuestion = noOfOptionsEachQuestion;
    }
    public Integer getNoOfGameWinForEligibility() {
        return noOfGameWinForEligibility;
    }
    public void setNoOfGameWinForEligibility(Integer noOfGameWinForEligibility) {
        this.noOfGameWinForEligibility = noOfGameWinForEligibility;
    }
    public Double getPointsForEachGameWin() {
        return pointsForEachGameWin;
    }
    public void setPointsForEachGameWin(Double pointsForEachGameWin) {
        this.pointsForEachGameWin = pointsForEachGameWin;
    }

    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }
    public Double getTotalRevenue() {
        return totalRevenue;
    }
    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    public Integer getTotalWiners() {
        return totalWiners;
    }
    public void setTotalWiners(Integer totalWiners) {
        this.totalWiners = totalWiners;
    }
    public String getWinerPubTime() {
        return winerPubTime;
    }
    public void setWinerPubTime(String winerPubTime) {
        this.winerPubTime = winerPubTime;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
    public Double getPointsForEachGameLost() {
        return pointsForEachGameLost;
    }
    public void setPointsForEachGameLost(Double pointsForEachGameLost) {
        this.pointsForEachGameLost = pointsForEachGameLost;
    }

    public String getCurrentTime() {
        return currentTime;
    }
    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isSponsored() {
        return sponsored;
    }

    public void setSponsored(boolean sponsored) {
        this.sponsored = sponsored;
    }
}
