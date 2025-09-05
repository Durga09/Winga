package in.eightfolds.winga.model;

import java.io.Serializable;
import java.util.List;

public class ExclusiveCategoryResponse implements Serializable {

    private Integer ecId;
    private String name;
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

    public List<ExclusiveCategoryItem> getItems() {
        return items;
    }

    public void setItems(List<ExclusiveCategoryItem> items) {
        this.items = items;
    }
}
