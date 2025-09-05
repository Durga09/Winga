package in.eightfolds.winga.v2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstaReelItem implements Serializable {

    public String instaReelUrl;
    public Integer instaReelTypeId;
    public String title;
    public Integer thumbnailId;
    public String thumbnailUrl;
    public Integer id;
    public String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @JsonIgnore
    public String subtitle;



    public String getInstaReelUrl() {
        return instaReelUrl;
    }

    public void setInstaReelUrl(String instaReelUrl) {
        this.instaReelUrl = instaReelUrl;
    }

    public Integer getInstaReelTypeId() {
        return instaReelTypeId;
    }

    public void setInstaReelTypeId(Integer instaReelTypeId) {
        this.instaReelTypeId = instaReelTypeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(Integer thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
