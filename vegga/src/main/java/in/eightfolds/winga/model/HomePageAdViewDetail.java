package in.eightfolds.winga.model;

import androidx.room.Ignore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class HomePageAdViewDetail implements Serializable {

	private Long id;
	private Long userId;

	private Long playDuration;
	private Long totalEngagementTime;
	private Long uniquePlayDuration;

	private Long noOfTimesVideoPlayed;

	private Long homePageAddId;
	private Integer platformId;
	private String imei;
	private String appViewDateTime;

	@Ignore
	private UUID uniqueUuid;


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

	public Long getTotalEngagementTime() {
		return totalEngagementTime;
	}
	public void setTotalEngagementTime(Long totalEngagementTime) {
		this.totalEngagementTime = totalEngagementTime;
	}
	public Long getHomePageAddId() {
		return homePageAddId;
	}
	public void setHomePageAddId(Long homePageAddId) {
		this.homePageAddId = homePageAddId;
	}
	public Integer getPlatformId() {
		return platformId;
	}
	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}

	public UUID getUniqueUuid() {
		return uniqueUuid;
	}

	public void setUniqueUuid(UUID uniqueUuid) {
		this.uniqueUuid = uniqueUuid;
	}


	public String getAppViewDateTime() {
		return appViewDateTime;
	}

	public void setAppViewDateTime(String appViewDateTime) {
		this.appViewDateTime = appViewDateTime;
	}


	

	

}
