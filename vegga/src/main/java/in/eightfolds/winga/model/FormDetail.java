package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormDetail implements Serializable {

	private Long fclId;
	private Integer fftId;
	private String lableName;
	private String lableValue;
	private String defaultValue;
	private String regex;

	public FormDetail() {
	}

	public FormDetail(Long fclId, Integer fftId, String lableName, String lableValue, String defaultValue, String regex) {
		super();
		this.fclId = fclId;
		this.fftId = fftId;
		this.lableName = lableName;
		this.lableValue = lableValue;
		this.defaultValue = defaultValue;
		this.regex = regex;
	}

	public Long getFclId() {
		return fclId;
	}

	public void setFclId(Long fclId) {
		this.fclId = fclId;
	}

	public Integer getFftId() {
		return fftId;
	}

	public void setFftId(Integer fftId) {
		this.fftId = fftId;
	}

	public String getLableName() {
		return lableName;
	}

	public void setLableName(String lableName) {
		this.lableName = lableName;
	}

	public String getLableValue() {
		return lableValue;
	}

	public void setLableValue(String lableValue) {
		this.lableValue = lableValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

}
