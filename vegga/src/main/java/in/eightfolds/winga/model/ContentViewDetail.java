package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentViewDetail  implements Serializable {

	private Long id;
	private Long userId;
	private Long playDuration;
	private Long uniquePlayDuration;
	private Long noOfTimesVideoPlayed;
	private Long timeTakenToAnswer;
	private Long totalEngagementTime;
	private Long contentId;
	private Long categoryGameId;
	private Long categoryId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getPlayDuration() {
		return playDuration;
	}

	public void setPlayDuration(Long playDuration) {
		this.playDuration = playDuration;
	}

	public Long getUniquePlayDuration() {
		return uniquePlayDuration;
	}

	public void setUniquePlayDuration(Long uniquePlayDuration) {
		this.uniquePlayDuration = uniquePlayDuration;
	}

	public Long getNoOfTimesVideoPlayed() {
		return noOfTimesVideoPlayed;
	}

	public void setNoOfTimesVideoPlayed(Long noOfTimesVideoPlayed) {
		this.noOfTimesVideoPlayed = noOfTimesVideoPlayed;
	}

	public Long getTimeTakenToAnswer() {
		return timeTakenToAnswer;
	}

	public void setTimeTakenToAnswer(Long timeTakenToAnswer) {
		this.timeTakenToAnswer = timeTakenToAnswer;
	}

	public Long getTotalEngagementTime() {
		return totalEngagementTime;
	}

	public void setTotalEngagementTime(Long totalEngagementTime) {
		this.totalEngagementTime = totalEngagementTime;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Long getCategoryGameId() {
		return categoryGameId;
	}

	public void setCategoryGameId(Long categoryGameId) {
		this.categoryGameId = categoryGameId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return "ContentViewDetail [id=" + id + ", userId=" + userId + ", playDuration=" + playDuration
				+ ", uniquePlayDuration=" + uniquePlayDuration + ", noOfTimesVideoPlayed=" + noOfTimesVideoPlayed
				+ ", timeTakenToAnswer=" + timeTakenToAnswer + ", totalEngagementTime=" + totalEngagementTime
				+ ", contentId=" + contentId + ", categoryGameId=" + categoryGameId + ", categoryId=" + categoryId
				+ "]";
	}

}
