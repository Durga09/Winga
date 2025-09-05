package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryGameSessionPrize extends BaseEntity implements Serializable {

	private Long cgspId;
	private Long categoryId;
	private Long cgsId;
	private String title;
	private String description;
	private int type;
	private Double amount;
	private String url;
	private int totalQty;
	private int prizeQty;
	private int deleted;

	public Long getCgspId() {
		return cgspId;
	}

	public void setCgspId(Long cgspId) {
		this.cgspId = cgspId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getCgsId() {
		return cgsId;
	}

	public void setCgsId(Long cgsId) {
		this.cgsId = cgsId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(int totalQty) {
		this.totalQty = totalQty;
	}

	public int getPrizeQty() {
		return prizeQty;
	}

	public void setPrizeQty(int prizeQty) {
		this.prizeQty = prizeQty;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "CategoryGameSessionPrize [cgspId=" + cgspId + ", categoryId=" + categoryId + ", cgsId=" + cgsId
				+ ", title=" + title + ", description=" + description + ", type=" + type + ", amount=" + amount
				+ ", url=" + url + ", totalQty=" + totalQty + ", prizeQty=" + prizeQty + ", deleted=" + deleted
				+ ", getCreatedBy()=" + getCreatedBy() + ", getModifiedBy()=" + getModifiedBy() + ", getCreatedTime()="
				+ getCreatedTime() + ", getModifiedTime()=" + getModifiedTime() + "]";
	}

}
