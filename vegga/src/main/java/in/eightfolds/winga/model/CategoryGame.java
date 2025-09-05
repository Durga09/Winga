package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryGame extends BaseEntity implements Serializable {

	public static final int STATE_NOT_SUBMITED = 0;
	public static final int STATE_SUBMITED = 1;
	
	private Long categoryGameId;
	private Long userId;
	private Long categoryId;
	private Long cgsId;
	private boolean firstGame;
	private Long userPrefStateId;
	private int state;
	private boolean gameWin;
	private String submitTime;

	public Long getCategoryGameId() {
		return categoryGameId;
	}

	public void setCategoryGameId(Long categoryGameId) {
		this.categoryGameId = categoryGameId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getCgsId() {
		return cgsId;
	}

	public void setCgsId(Long cgsId) {
		this.cgsId = cgsId;
	}

	public boolean isFirstGame() {
		return firstGame;
	}

	public void setFirstGame(boolean firstGame) {
		this.firstGame = firstGame;
	}

	public Long getUserPrefStateId() {
		return userPrefStateId;
	}

	public void setUserPrefStateId(Long userPrefStateId) {
		this.userPrefStateId = userPrefStateId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isGameWin() {
		return gameWin;
	}

	public void setGameWin(boolean gameWin) {
		this.gameWin = gameWin;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	@Override
	public String toString() {
		return "CategoryGame [categoryGameId=" + categoryGameId + ", userId=" + userId + ", categoryId=" + categoryId
				+ ", cgsId=" + cgsId + ", firstGame=" + firstGame + ", userPrefStateId=" + userPrefStateId + ", state="
				+ state + ", gameWin=" + gameWin + ", submitTime=" + submitTime + "]";
	}

}
