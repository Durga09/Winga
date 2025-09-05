package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuickTourModel implements Serializable{

    private int imageId;
    private String description;
    private int overlayImageId;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOverlayImageId() {
        return overlayImageId;
    }

    public void setOverlayImageId(int overlayImageId) {
        this.overlayImageId = overlayImageId;
    }
}
