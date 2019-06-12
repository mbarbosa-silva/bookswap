package com.app.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Campus {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "name", length = 128, nullable = false)
	private String name;
	
	@Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "addressLine1", column = @Column(name = "street")),
        @AttributeOverride(name = "addressLine2", column = @Column(name = "number")),
        @AttributeOverride(name = "addressLine3", column = @Column(name = "department"))
    })
	private Address address;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private School school;
	
	@OneToMany(mappedBy = "campus")
	private List<User> user = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public List<User> getUser() {
		return user;
	}

	public void setUser(List<User> user) {
		this.user = user;
	}

	public Campus(Long id, String name, Address address, School school, List<User> user) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.school = school;
		this.user = user;
	}
	
	public Campus() {}
	
}
