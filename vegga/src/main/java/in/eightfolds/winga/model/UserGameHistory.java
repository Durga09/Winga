package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class UserGameHistory implements Serializable {
    private int totalNoOfQuestions;
    private int totalNoOfCorrectAnswers;
    private Long gameId;
    private Long userId;
    private List<GameResult> gameResults;

    public int getTotalNoOfQuestions() {
        return totalNoOfQuestions;
    }
    public void setTotalNoOfQuestions(int totalNoOfQuestions) {
        this.totalNoOfQuestions = totalNoOfQuestions;
    }
    public int getTotalNoOfCorrectAnswers() {
        return totalNoOfCorrectAnswers;
    }
    public void setTotalNoOfCorrectAnswers(int totalNoOfCorrectAnswers) {
        this.totalNoOfCorrectAnswers = totalNoOfCorrectAnswers;
    }
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


    public List<GameResult> getGameResults() {
        return gameResults;
    }
    public void setGameResults(List<GameResult> gameResults) {
        this.gameResults = gameResults;
    }
    @Override
    public String toString() {
        return "UserGameHistory [totalNoOfQuestions=" + totalNoOfQuestions
                + ", totalNoOfCorrectAnswers=" + totalNoOfCorrectAnswers
                + ", gameId=" + gameId + ", userId=" + userId
                + ", gameResults=" + gameResults + "]";
    }

}
