package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAddress implements Serializable {
	private Long addressId;
	private Long userId;
	private Long adreTypeId;
	private Long countryId;
	private String countryCode;
	private Long stateId;
	private String city;
	private String address1;
	private String address2;
	private String landmark;
	private String pincode;
	private boolean addrIsDefault;
	private boolean deleted;
	private String createdTime;
	private String modifiedTime;
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getAdreTypeId() {
		return adreTypeId;
	}
	public void setAdreTypeId(Long adreTypeId) {
		this.adreTypeId = adreTypeId;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	public Long getStateId() {
		return stateId;
	}
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getLandmark() {
		return landmark;
	}
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public boolean isAddrIsDefault() {
		return addrIsDefault;
	}
	public void setAddrIsDefault(boolean addrIsDefault) {
		this.addrIsDefault = addrIsDefault;
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
	
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	@Override
	public String toString() {
		return "UserAddress [addressId=" + addressId + ", userId=" + userId
				+ ", adreTypeId=" + adreTypeId + ", countryId=" + countryId
				+ ", countryCode=" + countryCode + ", stateId=" + stateId
				+ ", city=" + city + ", address1=" + address1 + ", address2="
				+ address2 + ", landmark=" + landmark + ", pincode=" + pincode
				+ ", addrIsDefault=" + addrIsDefault + ", deleted=" + deleted
				+ ", createdTime=" + createdTime + ", modifiedTime="
				+ modifiedTime + "]";
	}
	
}
