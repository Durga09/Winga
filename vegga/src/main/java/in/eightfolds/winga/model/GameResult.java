package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.text.ParseException;

import in.eightfolds.utils.DateTime;


public class GameResult  implements Serializable {
		
//	private Long  grArchiveId;
	private Long  gameId;
	private Long  contentId;
	private Long  ansOppId;
	private Long  ansCorrect;
	private String  createdTime;
	private String selectedOppTxt;
	
	
	
//	public Long getGrArchiveId() {
//		return grArchiveId;
//	}
//	public void setGrArchiveId(Long grArchiveId) {
//		this.grArchiveId = grArchiveId;
//	}
	public Long getGameId() {
		return gameId;
	}
	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	public Long getContentId() {
		return contentId;
	}
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	public Long getAnsOppId() {
		return ansOppId;
	}
	public void setAnsOppId(Long ansOppId) {
		this.ansOppId = ansOppId;
	}
	
	public Long getAnsCorrect() {
		return ansCorrect;
	}
	public void setAnsCorrect(Long ansCorrect) {
		this.ansCorrect = ansCorrect;
	}
	
	@JsonProperty("createdTime")
	public Long getCreatedTimeLong() {
		try {
			return this.createdTime != null ? DateTime.getInMilliesFromUTC(this.createdTime): null;
		} catch (ParseException e) {
		}
		
		return null;
	}
	@JsonIgnore
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getSelectedOppTxt() {
		return selectedOppTxt;
	}
	public void setSelectedOppTxt(String selectedOppTxt) {
		this.selectedOppTxt = selectedOppTxt;
	}
	@Override
	public String toString() {
		return "GameResult [gameId=" + gameId
				+ ", contentId=" + contentId + ", ansOppId=" + ansOppId
				+ ", ansCorrect=" + ansCorrect + ", createdTime=" + createdTime
				+ ", selectedOppTxt=" + selectedOppTxt + "]";
	}

}
