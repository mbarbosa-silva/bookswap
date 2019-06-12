package com.app.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class VerificationToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private static final int EXPIRATION = 1;
     
    private String token;
   
    private LocalDateTime expiredDateTime;
    private LocalDateTime issuedDateTime;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public LocalDateTime getExpiredDateTime() {
		return expiredDateTime;
	}

	public void setExpiredDateTime(LocalDateTime expiredDateTime) {
		this.expiredDateTime = expiredDateTime;
	}

	public LocalDateTime getIssuedDateTime() {
		return issuedDateTime;
	}

	public void setIssuedDateTime(LocalDateTime issuedDateTime) {
		this.issuedDateTime = issuedDateTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static int getExpiration() {
		return EXPIRATION;
	}
    
    
    
}