package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 14/05/18.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class HomePageResponse  implements Serializable {

    private GameSession gameSession;
    private ArrayList<GameSession> upcomingSponsoredGameSessions;
    private GameSessionMessage gameSessionMessage;
    private List<GameSessionPrize> gameSesionPrizes ;
    private UserGameSession userGameSession;
    private User userDetail;
    private String nextGameSessionTime;
    private String currentTime;
    private Notification Notification;
    @JsonIgnore
    private long publishTimeRemainingMillis ;
    @JsonIgnore
    private long specialSessionStartInMillis;
    private int googleAdMobMaxViewCount;
    private int googleAdMobViewCount;
    private int googleAdMobPerViewPoints;

    public int getGoogleAdMobMaxViewCount() {
        return googleAdMobMaxViewCount;
    }

    public void setGoogleAdMobMaxViewCount(int googleAdMobMaxViewCount) {
        this.googleAdMobMaxViewCount = googleAdMobMaxViewCount;
    }

    public int getGoogleAdMobViewCount() {
        return googleAdMobViewCount;
    }

    public void setGoogleAdMobViewCount(int googleAdMobViewCount) {
        this.googleAdMobViewCount = googleAdMobViewCount;
    }

    public int getGoogleAdMobPerViewPoints() {
        return googleAdMobPerViewPoints;
    }

    public void setGoogleAdMobPerViewPoints(int googleAdMobPerViewPoints) {
        this.googleAdMobPerViewPoints = googleAdMobPerViewPoints;
    }





    public GameSession getGameSession() {
        return gameSession;
    }
    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }
    public List<GameSessionPrize> getGameSesionPrizes() {
        return gameSesionPrizes;
    }
    public void setGameSesionPrizes(List<GameSessionPrize> gameSesionPrizes) {
        this.gameSesionPrizes = gameSesionPrizes;
    }
    public UserGameSession getUserGameSession() {
        return userGameSession;
    }
    public void setUserGameSession(UserGameSession userGameSession) {
        this.userGameSession = userGameSession;
    }
    public User getUserDetail() {
        return userDetail;
    }
    public void setUserDetail(User userDetail) {
        this.userDetail = userDetail;
    }

    public String getNextGameSessionTime() {
        return nextGameSessionTime;
    }
    public void setNextGameSessionTime(String nextGameSessionTime) {
        this.nextGameSessionTime = nextGameSessionTime;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public GameSessionMessage getGameSessionMessage() {
        return gameSessionMessage;
    }
    public void setGameSessionMessage(GameSessionMessage gameSessionMessage) {
        this.gameSessionMessage = gameSessionMessage;
    }

    public long getPublishTimeRemainingMillis() {
        return publishTimeRemainingMillis;
    }

    public void setPublishTimeRemainingMillis(long publishTimeRemainingMillis) {
        this.publishTimeRemainingMillis = publishTimeRemainingMillis;
    }

    public Notification getNotification() {
        return Notification;
    }

    public void setNotification(Notification notification) {
        Notification = notification;
    }

    public ArrayList<GameSession> getUpcomingSponsoredGameSessions() {
        return upcomingSponsoredGameSessions;
    }

    public void setUpcomingSponsoredGameSessions(ArrayList<GameSession> upcomingSponsoredGameSessions) {
        this.upcomingSponsoredGameSessions = upcomingSponsoredGameSessions;
    }

    public long getSpecialSessionStartInMillis() {
        return specialSessionStartInMillis;
    }

    public void setSpecialSessionStartInMillis(long specialSessionStartInMillis) {
        this.specialSessionStartInMillis = specialSessionStartInMillis;
    }
}
