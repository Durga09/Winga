package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by sp on 14/05/18.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public  class UserGameSession implements Serializable {


    private Long id;
    private Long userId;
    private Long gSessionId;
    private Integer noOfGamePlayed;
    private Integer noOfWins;
    private boolean  eligible;
    private String  date;
    private boolean drawWin;

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
    public Long getgSessionId() {
        return gSessionId;
    }
    public void setgSessionId(Long gSessionId) {
        this.gSessionId = gSessionId;
    }
    public Integer getNoOfGamePlayed() {
        return noOfGamePlayed;
    }
    public void setNoOfGamePlayed(Integer noOfGamePlayed) {
        this.noOfGamePlayed = noOfGamePlayed;
    }
    public Integer getNoOfWins() {
        return noOfWins;
    }
    public void setNoOfWins(Integer noOfWins) {
        this.noOfWins = noOfWins;
    }

    public boolean isEligible() {
        return eligible;
    }
    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public boolean isDrawWin() {
        return drawWin;
    }
    public void setDrawWin(boolean drawWin) {
        this.drawWin = drawWin;
    }
    @Override
    public String toString() {
        return "UserGameSession [id=" + id + ", userId=" + userId
                + ", gSessionId=" + gSessionId + ", noOfGamePlayed="
                + noOfGamePlayed + ", noOfWins=" + noOfWins + ", eligible="
                + eligible + ", date=" + date + ", drawWin=" + drawWin + "]";
    }


}
