package com.entity.resource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.entity.User;

@Entity
@Table(name = "roles")
public class RoleList {
	
	  @Id
	  @Column(name = "role", length = 5, nullable = false)
	  private String role;

	  @ManyToOne
	  @JoinColumn(name = "user")
	  private User user;

}
