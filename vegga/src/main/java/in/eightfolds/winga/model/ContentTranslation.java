package in.eightfolds.winga.model;



import java.io.Serializable;
import java.util.List;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class ContentTranslation implements Serializable {

	private Long id;
	private Long contentId;
	private Long langId;
	private Integer type;
	private String youtubeVid;
	private Long fileId;
	private boolean sameAsEnglish;
	private Integer dirtyFlag ;
	private boolean deleted;
	private Long modifiedBy;
	private String createdTime;
	private String modifiedTime;
	private Long duration;
	
	private List<QuestionResponse> questions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Long getLangId() {
		return langId;
	}

	public void setLangId(Long langId) {
		this.langId = langId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getYoutubeVid() {
		return youtubeVid;
	}

	public void setYoutubeVid(String youtubeVid) {
		this.youtubeVid = youtubeVid;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	public boolean isSameAsEnglish() {
		return sameAsEnglish;
	}

	public void setSameAsEnglish(boolean sameAsEnglish) {
		this.sameAsEnglish = sameAsEnglish;
	}

	public List<QuestionResponse> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionResponse> questions) {
		this.questions = questions;
	}

	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
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

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}
	
	public Integer getDirtyFlag() {
		return dirtyFlag;
	}

	public void setDirtyFlag(Integer dirtyFlag) {
		this.dirtyFlag = dirtyFlag;
	}

	@Override
	public String toString() {
		return "ContentTranslation [id=" + id + ", contentId=" + contentId + ", langId=" + langId + ", type=" + type
				+ ", youtubeVid=" + youtubeVid + ", fileId=" + fileId + ", sameAsEnglish=" + sameAsEnglish
				+ ", dirtyFlag=" + dirtyFlag + ", deleted=" + deleted + ", modifiedBy=" + modifiedBy + ", createdTime="
				+ createdTime + ", modifiedTime=" + modifiedTime + ", duration=" + duration + ", questions=" + questions + "]";
	}
}
