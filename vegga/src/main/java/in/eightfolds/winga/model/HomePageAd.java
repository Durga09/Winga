package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class HomePageAd implements Serializable {
    public static final int TYPE_URL = 1;
    public static final int TYPE_FILE = 2;

    private long homePageAddId;
    private int type;
    private String youtubeId;
    private Long fileId;
    private Long clickUrl;
    private int viewCount;
    private String startTime;
    private String endTime;
    private boolean deleted;
    private String createdTime;
    private String modifiedTime;
    private String title;
    private String redirectLink;

    public String getRedirectLink() {
        return redirectLink;
    }

    public void setRedirectLink(String redirectLink) {
        this.redirectLink = redirectLink;
    }

    public long getHomePageAddId() {
        return homePageAddId;
    }

    public void setHomePageAddId(long homePageAddId) {
        this.homePageAddId = homePageAddId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(Long clickUrl) {
        this.clickUrl = clickUrl;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    @Override
    public String toString() {
        return "HomePageAd [homePageAddId=" + homePageAddId + ", type=" + type
                + ", youtubeId=" + youtubeId + ", fileId=" + fileId + ", clickUrl="
                + clickUrl + ", viewCount=" + viewCount + ", startTime="
                + startTime + ", endTime=" + endTime + ", deleted=" + deleted
                + ", createdTime=" + createdTime + ", modifiedTime="
                + modifiedTime + ", title=" + title + "]";
    }


}
