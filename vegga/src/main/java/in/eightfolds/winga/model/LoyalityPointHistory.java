package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class LoyalityPointHistory implements Serializable{

    private String pointsEarned;

    private String date;

    public String getPointsEarned ()
    {
        return pointsEarned;
    }

    public void setPointsEarned (String pointsEarned)
    {
        this.pointsEarned = pointsEarned;
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
        return "ClassPojo [pointsEarned = "+pointsEarned+", date = "+date+"]";
    }
}
