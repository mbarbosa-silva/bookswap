package com.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

//import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotEmpty;

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
	
	@NotEmpty
	@Column(name = "postalCode", length = 7, nullable = false)
	private String postalCode;
	
}
