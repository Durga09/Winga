package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class HelpDesk implements Serializable {

    private String val;

    private int id;

    private String createdTime;

    private String modifiedTime;

    private int type;

    private String deleted;

    public String getVal ()
    {
        return val;
    }

    public void setVal (String val)
    {
        this.val = val;
    }



    public String getCreatedTime ()
    {
        return createdTime;
    }

    public void setCreatedTime (String createdTime)
    {
        this.createdTime = createdTime;
    }

    public String getModifiedTime ()
    {
        return modifiedTime;
    }

    public void setModifiedTime (String modifiedTime)
    {
        this.modifiedTime = modifiedTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDeleted ()
    {
        return deleted;
    }

    public void setDeleted (String deleted)
    {
        this.deleted = deleted;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [val = "+val+", id = "+id+", createdTime = "+createdTime+", modifiedTime = "+modifiedTime+", type = "+type+", deleted = "+deleted+"]";
    }
}
