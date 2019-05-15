package com.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

//import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotEmpty;

@Entity(name="product")
@Table(name="product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotEmpty
	@Column(name = "title", length = 255, nullable = false)
	private String title;
	
	@Column(name = "isbn", length = 128, nullable = false)
	private String isbn;
	
	@Column(name = "author", length = 128, nullable = false)
	private String author;
	
	@Column(name = "edition", length = 128, nullable = false)
	private String edition;
	
	@Column(name = "publisher", length = 128, nullable = false)
	private String publisher;
	
	@Column(name = "description", length = 255, nullable = false)
	private String description;
	
	@OneToOne(mappedBy="product",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Ad ad;
	
	@OneToMany(mappedBy="product",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();
	
}
