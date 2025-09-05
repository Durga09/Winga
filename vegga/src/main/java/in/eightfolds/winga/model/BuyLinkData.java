package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuyLinkData implements Serializable {
	private Long contentId;
	private String buyLinkButtonText;
	private String buyLinkAlertText;
	private Long buyLinkThumbnail;

	public BuyLinkData() {
	}

	public BuyLinkData(Long contentId, String buyLinkButtonText, String buyLinkAlertText, Long buyLinkThumbnail) {
		this.contentId = contentId;
		this.buyLinkButtonText = buyLinkButtonText;
		this.buyLinkAlertText = buyLinkAlertText;
		this.buyLinkThumbnail = buyLinkThumbnail;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public String getBuyLinkButtonText() {
		return buyLinkButtonText;
	}

	public void setBuyLinkButtonText(String buyLinkButtonText) {
		this.buyLinkButtonText = buyLinkButtonText;
	}
	
	public String getBuyLinkAlertText() {
		return buyLinkAlertText;
	}

	public void setBuyLinkAlertText(String buyLinkAlertText) {
		this.buyLinkAlertText = buyLinkAlertText;
	}

	public Long getBuyLinkThumbnail() {
		return buyLinkThumbnail;
	}

	public void setBuyLinkThumbnail(Long buyLinkThumbnail) {
		this.buyLinkThumbnail = buyLinkThumbnail;
	}

}
