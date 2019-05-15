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
}
