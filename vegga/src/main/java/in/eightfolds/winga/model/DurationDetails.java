package in.eightfolds.winga.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)


@Entity(tableName = "durationdetails")
public class DurationDetails implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "gameid")
    private long gameId;

    @Ignore
    private UUID uniqueUuid;


    @ColumnInfo(name = "contentId")
    private long contentId;

    @ColumnInfo(name = "startmills")
    private long startMillis;

    @ColumnInfo(name = "endmills")
    private long endMills;

    @ColumnInfo(name = "activemills")
    private long activeMills;

    @ColumnInfo(name = "questionActivemillis")
    private long questionActivemillis;

    @ColumnInfo(name = "questionId")
    private long questionId;

    @ColumnInfo(name = "videoStartUniqueId")
    private long videoStartUniqueId ;

    @ColumnInfo(name = "homepageaddid")
    private long homePageAddId;

    @ColumnInfo(name = "aduniqueid")
    private long adUniqueId;

    @ColumnInfo(name = "isadddetail")
    private int isAdDetail;

    public long getQuestionActivemillis() {
        return questionActivemillis;
    }

    public void setQuestionActivemillis(long questionActivemillis) {
        this.questionActivemillis = questionActivemillis;
    }

    public long getVideoStartUniqueId() {
        return videoStartUniqueId;
    }

    public void setVideoStartUniqueId(long videoStartUniqueId) {
        this.videoStartUniqueId = videoStartUniqueId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getUniqueUuid() {
        return uniqueUuid;
    }

    public void setUniqueUuid(UUID uniqueUuid) {
        this.uniqueUuid = uniqueUuid;
    }

    public int getIsAdDetail() {
        return isAdDetail;
    }

    public void setIsAdDetail(int isAdDetail) {
        this.isAdDetail = isAdDetail;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public long getStartMillis() {
        return startMillis;
    }

    public void setStartMillis(long startMillis) {
        this.startMillis = startMillis;
    }

    public long getEndMills() {
        return endMills;
    }

    public void setEndMills(long endMills) {
        this.endMills = endMills;
    }

    public long getActiveMills() {
        return activeMills;
    }

    public void setActiveMills(long activeMills) {
        this.activeMills = activeMills;
    }

    public long getHomePageAddId() {
        return homePageAddId;
    }

    public void setHomePageAddId(long homePageAddId) {
        this.homePageAddId = homePageAddId;
    }

    public long getAdUniqueId() {
        return adUniqueId;
    }

    public void setAdUniqueId(long adUniqueId) {
        this.adUniqueId = adUniqueId;
    }

    @Override
    public String toString() {
        return "DurationDetails{" +
                "id=" + id +
                ", gameId=" + gameId +
                ", uniqueUuid=" + uniqueUuid +
                ", contentId=" + contentId +
                ", startMillis=" + startMillis +
                ", endMills=" + endMills +
                ", activeMills=" + activeMills +
                ", questionActivemillis=" + questionActivemillis +
                ", questionId=" + questionId +
                ", videoStartUniqueId=" + videoStartUniqueId +
                ", homePageAddId=" + homePageAddId +
                ", adUniqueId=" + adUniqueId +
                ", isAdDetail=" + isAdDetail +
                '}';
    }
}
