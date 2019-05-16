package com.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="ad")
@Table(name="ad")
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "message", length = 500, nullable = false)
	private String message;
	
	@Column(name = "ReportItemId", nullable = false)
	private Long reportItemIdReference;
}
