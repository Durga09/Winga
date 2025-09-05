package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShareLinkData implements Serializable {

    private Long contentId;
    private String shareLinkButtonText;

public ShareLinkData(){

}
    public ShareLinkData(Long contentId, String shareLinkButtonText) {
        this.contentId = contentId;
        this.shareLinkButtonText = shareLinkButtonText;
    }

    public String getContentId() {
        return contentId.toString();
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getShareLinkButtonText() {
        return shareLinkButtonText;
    }

    public void setShareLinkButtonText(String shareLinkButtonText) {
        this.shareLinkButtonText = shareLinkButtonText;
    }
}
