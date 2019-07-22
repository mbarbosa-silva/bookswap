package com.app.model;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    String firstName;
    private String lastName;
    
    private String email;
    private String username;
    private String password;

    private Boolean enable;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private Campus campus;

	@OneToMany(mappedBy="user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Comment> comments = new ArrayList<>();
	
	@Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "addressLine1", column = @Column(name = "street")),
        @AttributeOverride(name = "addressLine2", column = @Column(name = "number"))
    })
	private Address address;
	
	@OneToMany(mappedBy="user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Ad> ad = new ArrayList<>();
	
	@OneToMany(mappedBy="buyer",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Invoice> buyHistory;
	
	@OneToMany(mappedBy="seller",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Invoice> sellHistory;
	
	@OneToOne
	private File photo;
	
    public User() {
    }

    public User(String firstName, String lastName, String username, String password, Collection<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

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
    
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<Ad> getAd() {
		return ad;
	}

	public void setAd(List<Ad> ad) {
		this.ad = ad;
	}

	public void addAd(Ad ad) {
		this.ad.add(ad);
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
	
	public File getPhoto() {
		return photo;
	}

	public void setPhoto(File photo) {
		this.photo = photo;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return (Collection<? extends GrantedAuthority>) this.roles;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return enable;
	}
	
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", username=" + username + ", password=" + password + ", enable=" + enable + ", roles=" + roles
				+ ", campus=" + campus + ", comments=" + comments + ", address=" + address + ", ad=" + ad
				+ ", buyHistory=" + buyHistory + ", sellHistory=" + sellHistory + ", photo=" + photo + "]";
	}

}
