package com.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

import javax.validation.constraints.NotEmpty;

@Entity(name="user")
@Table(name="user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "firstName", length = 128, nullable = false)
	private String firstName;
	
	@Column(name = "lastName", length = 128, nullable = false)
	private String lastName;
	
	@NotEmpty
	@Column(name = "email", length = 128, nullable = false)
    private String email;
	
	@NotEmpty
	@Column(name = "username", length = 30, nullable = false)
    private String username;
	
	@NotEmpty
	@Column(name = "password", length = 30, nullable = false)
    private String password;
	
	@Column(name = "enabled", nullable = false)
	@Type( type = "org.hibernate.type.NumericBooleanType")
	private boolean enabled;
	
	@NotEmpty
	@Column(name = "role", columnDefinition = "varchar(5) DEFAULT 'USER'")
	private String role;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Campus campus;
	
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "user_address", joinColumns = @JoinColumn(name = "user_id"))
    @AttributeOverrides({
        @AttributeOverride(name = "addressLine1", column = @Column(name = "street")),
        @AttributeOverride(name = "addressLine2", column = @Column(name = "number"))
    })
	private List<Address> address = new ArrayList<>();
	
	@OneToMany(mappedBy="user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<Ad> ad = new ArrayList<>();
	
	@OneToMany(mappedBy="buyer",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<Invoice> buyHistory;
	
	@OneToMany(mappedBy="seller",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<Invoice> sellHistory;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Address> getAddress() {
		return address;
	}

	public void setAddress(List<Address> address) {
		this.address = address;
	}

	public List<Ad> getAd() {
		return ad;
	}

	public void setAd(List<Ad> ad) {
		this.ad = ad;
	}

	public List<Invoice> getBuyHistory() {
		return buyHistory;
	}

	public void setBuyHistory(List<Invoice> buyHistory) {
		this.buyHistory = buyHistory;
	}

	public List<Invoice> getSellHistory() {
		return sellHistory;
	}

	public void setSellHistory(List<Invoice> sellHistory) {
		this.sellHistory = sellHistory;
	}
	
	
}
