package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaFormFeildType  implements Serializable {

	private Integer fftId;
	private String name;

	public MetaFormFeildType() {
	}

	public MetaFormFeildType(String name) {
		super();
		this.name = name;
	}

	public Integer getFftId() {
		return fftId;
	}

	public void setFftId(Integer fftId) {
		this.fftId = fftId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
