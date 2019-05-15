package com.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name="campus")
@Table(name="campus")
public class Campus {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "Name", length = 128, nullable = false)
	private String Name;
	
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
	
	@OneToMany(mappedBy = "campus",fetch = FetchType.LAZY)
	private List<User> user = new ArrayList<>();
}
