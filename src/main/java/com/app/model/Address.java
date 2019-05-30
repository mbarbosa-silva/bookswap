package com.app.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {
	
	@Column(name = "addressLine1", length = 128, nullable = false)
	private String addressLine1;
	
	@Column(name = "addressLine2", length = 128, nullable = false)
	private String addressLine2;
	
	@Column(name = "addressLine3", length = 128, nullable = true)
	private String addressLine3;
	
	@Column(name = "city", length = 128, nullable = false)
	private String city;
	
	@Column(name = "province", length = 128, nullable = false)
	private String province;
	
	@Column(name = "country", length = 128, nullable = false)
	private String country;
	
	@Column(name = "postalCode", length = 7, nullable = false)
	private String postalCode;

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
}
