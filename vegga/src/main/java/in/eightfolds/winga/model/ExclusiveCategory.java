package in.eightfolds.winga.model;

import java.io.Serializable;
import java.util.List;

public class ExclusiveCategory implements Serializable {

    private Integer ecId;
    private String name;
    private Integer state;
    private String createdTime;
    private List<ExclusiveCategoryItem> items;

    public Integer getEcId() {
        return ecId;
    }

    public void setEcId(Integer ecId) {
        this.ecId = ecId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public List<ExclusiveCategoryItem> getItems() {
        return items;
    }

    public void setItems(List<ExclusiveCategoryItem> items) {
        this.items = items;
    }
}
