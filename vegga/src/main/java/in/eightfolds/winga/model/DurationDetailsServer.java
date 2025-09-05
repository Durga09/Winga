package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class DurationDetailsServer implements Serializable{

    private long gameId;
    private long contentId;
    private long activeMills;
    private long questionsActiveMills;

    public long getQuestionsActiveMills() {
        return questionsActiveMills;
    }

    public void setQuestionsActiveMills(long questionsActiveMills) {
        this.questionsActiveMills = questionsActiveMills;
    }

    //  private long activeMillisOfVideo;


    //private long numberOfTimesVideoPlayed;
    //private long maxDurationViewMillis;

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public long getActiveMills() {
        return activeMills;
    }

    public void setActiveMills(long activeMills) {
        this.activeMills = activeMills;
    }

  /*  public long getNumberOfTimesVideoPlayed() {
        return numberOfTimesVideoPlayed;
    }

    public void setNumberOfTimesVideoPlayed(long numberOfTimesVideoPlayed) {
        this.numberOfTimesVideoPlayed = numberOfTimesVideoPlayed;
    }

    public long getMaxDurationViewMillis() {
        return maxDurationViewMillis;
    }

    public void setMaxDurationViewMillis(long maxDurationViewMillis) {
        this.maxDurationViewMillis = maxDurationViewMillis;
    }*/

    @Override
    public String toString() {
        return "DurationDetailsServer{" +
                "gameId=" + gameId +
                ", contentId=" + contentId +
                ", activeMills=" + activeMills +
                ", questionsActiveMills=" + questionsActiveMills +
                '}';
    }
}
