package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class GameHistoryItem implements Serializable {

    private String pointsWin;

    private String noOfWins;

    private String noOfGamePlayed;

    private String date;

    public String getPointsWin ()
    {
        return pointsWin;
    }

    public void setPointsWin (String pointsWin)
    {
        this.pointsWin = pointsWin;
    }

    public String getNoOfWins ()
    {
        return noOfWins;
    }

    public void setNoOfWins (String noOfWins)
    {
        this.noOfWins = noOfWins;
    }

    public String getNoOfGamePlayed ()
    {
        return noOfGamePlayed;
    }

    public void setNoOfGamePlayed (String noOfGamePlayed)
    {
        this.noOfGamePlayed = noOfGamePlayed;
    }

    public String getDate ()
    {
        return date;
    }

    public void setDate (String date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pointsWin = "+pointsWin+", noOfWins = "+noOfWins+", noOfGamePlayed = "+noOfGamePlayed+", date = "+date+"]";
    }
}
