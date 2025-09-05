package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryGameSessionMessage implements Serializable {

	private Long categoryId;
	private Long code;
	private Long langId;
	private String message;

	public CategoryGameSessionMessage() {
	}

	public CategoryGameSessionMessage(Long categoryId, Long code, Long langId, String message) {
		super();
		this.categoryId = categoryId;
		this.code = code;
		this.langId = langId;
		this.message = message;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public Long getLangId() {
		return langId;
	}

	public void setLangId(Long langId) {
		this.langId = langId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "CategoryGameSessionMessage [categoryId=" + categoryId + ", code=" + code + ", langId=" + langId
				+ ", message=" + message + "]";
	}

}
