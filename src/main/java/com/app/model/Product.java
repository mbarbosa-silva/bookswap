package com.app.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
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
	
	@OneToOne(mappedBy="product",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(nullable = false)
	@JsonBackReference
	private Ad ad;
	
	@OneToMany(mappedBy="product",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Ad getAd() {
		return ad;
	}

	public void setAd(Ad ad) {
		this.ad = ad;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	
	
}
