package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class GameSessionWinnerResponse implements Serializable {
    private Long cgsId;
    private Long cgssId;
    private Long categoryId;
    private String startTime;
    private String endTime;
    private String winerPubTime;
    private CategoryGameWinner winner;
    private List<CategoryGameWinner> winners;
    private Integer totalWinner;
    private List<CategoryGameSessionPrize> gameSessionPrizes;

    public Long getCgsId() {
        return cgsId;
    }

    public void setCgsId(Long cgsId) {
        this.cgsId = cgsId;
    }

    public Long getCgssId() {
        return cgssId;
    }

    public void setCgssId(Long cgssId) {
        this.cgssId = cgssId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWinerPubTime() {
        return winerPubTime;
    }

    public void setWinerPubTime(String winerPubTime) {
        this.winerPubTime = winerPubTime;
    }

    public CategoryGameWinner getWinner() {
        return winner;
    }

    public void setWinner(CategoryGameWinner winner) {
        this.winner = winner;
    }

    public List<CategoryGameWinner> getWinners() {
        return winners;
    }

    public void setWinners(List<CategoryGameWinner> winners) {
        this.winners = winners;
    }

    public Integer getTotalWinner() {
        return totalWinner;
    }

    public void setTotalWinner(Integer totalWinner) {
        this.totalWinner = totalWinner;
    }

    public List<CategoryGameSessionPrize> getGameSessionPrizes() {
        return gameSessionPrizes;
    }

    public void setGameSessionPrizes(List<CategoryGameSessionPrize> gameSessionPrizes) {
        this.gameSessionPrizes = gameSessionPrizes;
    }
}
