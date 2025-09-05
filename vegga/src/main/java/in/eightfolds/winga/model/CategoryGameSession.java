package in.eightfolds.winga.model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.text.ParseException;

import in.eightfolds.utils.DateTime;


@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryGameSession extends BaseEntity implements Serializable {
	
	@JsonIgnore

	public static final int STATE_OPEN = 0;
	@JsonIgnore

	public static final int STATE_CLOSE = 1;
	@JsonIgnore

	public static final int STATE_SELECTING_WINERS = 2;
	@JsonIgnore

	public static final int STATE_WINER_PUBLISHED = 3;
	@JsonIgnore

	public static final int STATE_CANCLED = -1;
	@JsonIgnore

	public static final int STATE_FAILED = -2;
	

	public static final String TIME_ZONE = "05:30";
	
	private Long cgsId;
	private Long cgssId;
	private Long categoryId;
	private Integer priority;
	private String startTime;
	private String endTime;
	private Integer noOfContentsEachGame;
	private Integer noOfQuestionsEachContent;
	private Integer noOfOptionsEachQuestion;
	private Integer noOfGameWinForEligibility;
	private Integer pointsForEachGameWin;
	private Integer pointsForEachGameLost;
	private Integer state;
	private String currentTime;
	private Integer revenuePercentageAsPrize;
	private Integer totalEligibleCandidates;
	private Integer totalWiners;
	private String winerPubTime;
	private boolean paused;
	private Long lastPausedBy;
	private Long lastResumedBy;
	private boolean sponsored;
	private Long sponsoredCampaignId;
	private String sessionMessage;
	private int deleted;
	private Long deletedBy;

	public Long getCgsId() {
		return cgsId;
	}

	public void setCgsId(Long cgsId) {
		this.cgsId = cgsId;
	}

	public Long getCgssId() {
		return cgssId;
	}

	public void setCgssId(Long cgssId) {
		this.cgssId = cgssId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
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

	public Integer getPointsForEachGameWin() {
		return pointsForEachGameWin;
	}

	public void setPointsForEachGameWin(Integer pointsForEachGameWin) {
		this.pointsForEachGameWin = pointsForEachGameWin;
	}

	public Integer getPointsForEachGameLost() {
		return pointsForEachGameLost;
	}

	public void setPointsForEachGameLost(Integer pointsForEachGameLost) {
		this.pointsForEachGameLost = pointsForEachGameLost;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getRevenuePercentageAsPrize() {
		return revenuePercentageAsPrize;
	}

	public void setRevenuePercentageAsPrize(Integer revenuePercentageAsPrize) {
		this.revenuePercentageAsPrize = revenuePercentageAsPrize;
	}

	public Integer getTotalEligibleCandidates() {
		return totalEligibleCandidates;
	}

	public void setTotalEligibleCandidates(Integer totalEligibleCandidates) {
		this.totalEligibleCandidates = totalEligibleCandidates;
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

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public Long getLastPausedBy() {
		return lastPausedBy;
	}

	public void setLastPausedBy(Long lastPausedBy) {
		this.lastPausedBy = lastPausedBy;
	}

	public Long getLastResumedBy() {
		return lastResumedBy;
	}

	public void setLastResumedBy(Long lastResumedBy) {
		this.lastResumedBy = lastResumedBy;
	}

	public boolean isSponsored() {
		return sponsored;
	}

	public void setSponsored(boolean sponsored) {
		this.sponsored = sponsored;
	}

	public Long getSponsoredCampaignId() {
		return sponsoredCampaignId;
	}

	public void setSponsoredCampaignId(Long sponsoredCampaignId) {
		this.sponsoredCampaignId = sponsoredCampaignId;
	}

	public String getSessionMessage() {
		return sessionMessage;
	}

	public void setSessionMessage(String sessionMessage) {
		this.sessionMessage = sessionMessage;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public Long getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(Long deletedBy) {
		this.deletedBy = deletedBy;
	}

	public boolean isActive() {
		try {
			if(this.startTime != null && this.endTime != null) {
				long now = System.currentTimeMillis();
				if (DateTime.getInMilliesFromUTC(this.startTime) <= now
						&& DateTime.getInMilliesFromUTC(this.endTime) >= now) {
					
					return true;
				}
			}
		} catch (ParseException e) {

		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "CategoryGameSession [cgsId=" + cgsId + ", cgssId=" + cgssId + ", categoryId=" + categoryId
				+ ", priority=" + priority + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", noOfContentsEachGame=" + noOfContentsEachGame + ", noOfQuestionsEachContent="
				+ noOfQuestionsEachContent + ", noOfOptionsEachQuestion=" + noOfOptionsEachQuestion
				+ ", noOfGameWinForEligibility=" + noOfGameWinForEligibility + ", pointsForEachGameWin="
				+ pointsForEachGameWin + ", pointsForEachGameLost=" + pointsForEachGameLost + ", state=" + state
				+ ", revenuePercentageAsPrize=" + revenuePercentageAsPrize + ", totalEligibleCandidates="
				+ totalEligibleCandidates + ", totalWiners=" + totalWiners + ", winerPubTime=" + winerPubTime
				+ ", paused=" + paused + ", lastPausedBy=" + lastPausedBy + ", lastResumedBy=" + lastResumedBy
				+ ", sponsored=" + sponsored + ", sponsoredCampaignId=" + sponsoredCampaignId + ", sessionMessage="
				+ sessionMessage + ", deleted=" + deleted + ", deletedBy=" + deletedBy + "]";
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
}
