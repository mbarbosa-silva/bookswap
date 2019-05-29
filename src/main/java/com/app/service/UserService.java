package com.app.service;

import com.app.model.Ad;
import com.app.model.Role;
import com.app.model.User;
import com.app.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
    
    public void save(User user) {   	
    	userRepository.save(user);
    }
    
    public User findByUserName(String username) throws UsernameNotFoundException{
    	User user = userRepository.findByUsername(username);
    	
        if (user == null){
            throw new UsernameNotFoundException("user not found");
        }
        
        return user;
    }
    
    public List<Ad> findAdByUserName(String username) throws UsernameNotFoundException{
    	User user = userRepository.findByUsername(username);
    	return user.getAd();
    }
    
    public Collection<User> findAll(){
    	return userRepository.findAll();
    }
}
