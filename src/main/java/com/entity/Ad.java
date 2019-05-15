package com.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

//import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity(name="ad")
@Table(name="ad")
public class Ad {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotEmpty
	@Column(name = "price", length = 128, nullable = false)
	private Double price;
	
	@Column(name = "description", length = 500, nullable = false)
	private String description;
	
	@CreationTimestamp
	@Column(name = "CreatedDate", updatable=false)
	private Timestamp createdDate;

	@UpdateTimestamp
	@Column(name = "ModifiedDate")
	private Timestamp modifiedDate;
	
	@JoinColumn
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Product product;
	
	@JoinColumn
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
	
	@OneToOne(mappedBy="ad")
	private Invoice invoice;
		
}
