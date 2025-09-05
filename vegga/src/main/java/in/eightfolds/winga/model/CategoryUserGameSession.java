package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryUserGameSession  implements Serializable {

	private Long userId;
	private Long cgsId;
	private Long categoryId;
	private String createdDate;
	private int noOfGamePlayed;
	private int noOfWins;
	private int pointsWin;
	private boolean eligible;
	private boolean drawWin;
	private int allowedGamesPerUser;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCgsId() {
		return cgsId;
	}

	public void setCgsId(Long cgsId) {
		this.cgsId = cgsId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getNoOfGamePlayed() {
		return noOfGamePlayed;
	}

	public void setNoOfGamePlayed(int noOfGamePlayed) {
		this.noOfGamePlayed = noOfGamePlayed;
	}

	public int getNoOfWins() {
		return noOfWins;
	}

	public void setNoOfWins(int noOfWins) {
		this.noOfWins = noOfWins;
	}

	public int getPointsWin() {
		return pointsWin;
	}

	public void setPointsWin(int pointsWin) {
		this.pointsWin = pointsWin;
	}

	public boolean isEligible() {
		return eligible;
	}

	public void setEligible(boolean eligible) {
		this.eligible = eligible;
	}

	public boolean isDrawWin() {
		return drawWin;
	}

	public void setDrawWin(boolean drawWin) {
		this.drawWin = drawWin;
	}

	public int getAllowedGamesPerUser() {
		return allowedGamesPerUser;
	}

	public void setAllowedGamesPerUser(int allowedGamesPerUser) {
		this.allowedGamesPerUser = allowedGamesPerUser;
	}

	@Override
	public String toString() {
		return "CategoryUserGameSession [userId=" + userId + ", cgsId=" + cgsId + ", categoryId=" + categoryId
				+ ", createdDate=" + createdDate + ", noOfGamePlayed=" + noOfGamePlayed + ", noOfWins=" + noOfWins
				+ ", pointsWin=" + pointsWin + ", eligible=" + eligible + ", drawWin=" + drawWin
				+ ", allowedGamesPerUser=" + allowedGamesPerUser + "]";
	}

}
