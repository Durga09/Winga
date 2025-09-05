package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryTranslation implements Serializable {

	private Long catagoryId;
	private String categoryName;
	private String winerPubTime;
	private List<Translation> translations;
	private Long cgsId;
	private Long categoryType;
	private  List<CategoryTranslation> childCategories;



	public Long getCatagoryId() {
		return catagoryId;
	}

	public void setCatagoryId(Long catagoryId) {
		this.catagoryId = catagoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getWinerPubTime() {
		return winerPubTime;
	}

	public void setWinerPubTime(String winerPubTime) {
		this.winerPubTime = winerPubTime;
	}

	public List<Translation> getTranslations() {
		return translations;
	}

	public void setTranslations(List<Translation> translations) {
		this.translations = translations;
	}

	public Long getCgsId() {
		return cgsId;
	}

	public void setCgsId(Long cgsId) {
		this.cgsId = cgsId;
	}

	public Long getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(Long categoryType) {
		this.categoryType = categoryType;
	}

	public List<CategoryTranslation> getChildCategories() {
		return childCategories;
	}

	public void setChildCategories(List<CategoryTranslation> childCategories) {
		this.childCategories = childCategories;
	}

	@Override
	public String toString() {
		return "CategoryTranslation [catagoryId=" + catagoryId + ", categoryName=" + categoryName + ", winerPubTime="
				+ winerPubTime + ", translations=" + translations + ", cgsId=" + cgsId + "]";
	}

}
