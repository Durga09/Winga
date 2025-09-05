package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpinAndWinResponse implements Serializable {
    private int noOfGamesPerUser;
    private boolean spinAndWin;

    public int getNoOfGamesPerUser() {
        return noOfGamesPerUser;
    }

    public void setNoOfGamesPerUser(int noOfGamesPerUser) {
        this.noOfGamesPerUser = noOfGamesPerUser;
    }

    public boolean isSpinAndWin() {
        return spinAndWin;
    }

    public void setSpinAndWin(boolean spinAndWin) {
        this.spinAndWin = spinAndWin;
    }
}
