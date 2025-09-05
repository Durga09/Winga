package in.eightfolds.winga.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification implements Serializable {
	public static final int NOTIFICATION_TYPE_NOTIFICATION = 1;
	public static final int NOTIFICATION_TYPE_REFERRAL = 2;
	public static final int NOTIFICATION_TYPE_SESSION_START = 3;
	public static final int NOTIFICATION_TYPE_SESSION_END = 4;
	public static final int NOTIFICATION_TYPE_LUCKY_DRAW = 5;
	public static final int NOTIFICATION_TYPE_RESULT_DECLARED = 6;
	public static final int NOTIFICATION_TYPE_SETUP_CHANGE = 7;
	public static final int NOTIFICATION_TYPE_FIRST_GAME_NOT_PLAYED = 8;
	public static final int NOTIFICATION_TYPE_DISBURSEMENT=9;
	public static final int NOTIFICATION_TYPE_TEN=10;
	
	private Long notificationId;
	private Long  userId;
	private String  title;
	private String  message;
	private Integer  type;
	private Long  imageId;
	private String jsonData;
	private boolean  deleted;
	private String  createdTime;
	private String  modifiedTime;
	private PrizeWin prizeObj;

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean read) {
		isRead = read;
	}

	@JsonIgnore
	private boolean isRead;
	public Long getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getImageId() {
		return imageId;
	}
	public void setImageId(Long imageId) {
		this.imageId = imageId;
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
	
	public String getJsonData() {
		return jsonData;
	}
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
	public PrizeWin getPrizeObj() {
		return prizeObj;
	}
	public void setPrizeObj(PrizeWin prizeObj) {
		this.prizeObj = prizeObj;
	}


	@Override
	public String toString() {
		return "Notification [notificationId=" + notificationId + ", userId="
				+ userId + ", title=" + title + ", message=" + message
				+ ", type=" + type + ", imageId=" + imageId + ", jsonData="
				+ jsonData + ", deleted=" + deleted + ", createdTime="
				+ createdTime + ", modifiedTime=" + modifiedTime
				+ ", prizeObj=" + prizeObj + "]";
	}
	
	
}
