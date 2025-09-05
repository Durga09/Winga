package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryContentGame implements Serializable {

    private Long ccgId;
    private Long categoryId;
    private String name;
    private boolean state;
    private int type;
    private int priority;
    private String thumbnailUrl;
    private String gameUrl;

    public CategoryContentGame() {

    }

    public CategoryContentGame(Long ccgId, Long categoryId, String name, boolean state, int type, int priority, String thumbnailUrl, String gameUrl) {
        this.ccgId = ccgId;
        this.categoryId = categoryId;
        this.name = name;
        this.state = state;
        this.type = type;
        this.priority = priority;
        this.thumbnailUrl = thumbnailUrl;
        this.gameUrl = gameUrl;
    }

    public Long getCcgId() {
        return ccgId;
    }

    public void setCcgId(Long ccgId) {
        this.ccgId = ccgId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }
}
