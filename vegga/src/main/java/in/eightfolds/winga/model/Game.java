package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by sp on 14/05/18.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class Game implements Serializable {

    private Long gameId;
    private Long userId;
    private Long gSessionId;
    private Boolean firstGame;
    private Integer state;
    private Long createdTime;
    private Long modifiedTime;

    public Long getGameId() {
        return gameId;
    }
    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getgSessionId() {
        return gSessionId;
    }
    public void setgSessionId(Long gSessionId) {
        this.gSessionId = gSessionId;
    }
    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }



    public Boolean isFirstGame() {
        return firstGame;
    }
    public void setFirstGame(Boolean firstGame) {
        this.firstGame = firstGame;
    }
    public Long getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }
    public Long getModifiedTime() {
        return modifiedTime;
    }
    public void setModifiedTime(Long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
    @Override
    public String toString() {
        return "Game [gameId=" + gameId + ", userId=" + userId
                + ", gSessionId=" + gSessionId + ", firstGame=" + firstGame
                + ", state=" + state + ", createdTime=" + createdTime
                + ", modifiedTime=" + modifiedTime + "]";
    }

}
