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
import javax.persistence.Table;

import javax.validation.constraints.NotEmpty;

@Entity
@Table
public class School {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotEmpty
	@Column(name = "Name", length = 128, nullable = false)
	private String Name;
	
	@OneToMany(mappedBy="school", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Campus> campus = new ArrayList<>();
	
}
