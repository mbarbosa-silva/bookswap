package com.app.service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.model.User;
import com.app.model.VerificationToken;
import com.app.repository.UserRepository;
import com.app.repository.VerificationTokenRepository;

@Service
public class VerificationTokenService {

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Autowired
	private UserRepository userRepository;
	
	public String createToken() {
		return UUID.randomUUID().toString();
	}
	
	public String generateToken(User user) {
		
		VerificationToken token = new VerificationToken();
		
		token.setUser(user);
		token.setIssuedDateTime(LocalDateTime.now());
		token.setExpiredDateTime(LocalDateTime.now().plusDays(VerificationToken.getExpiration()));
		token.setToken(createToken());
		
		tokenRepository.save(token);
		
		return token.getToken();
	}
	
	public void validateToken(String tkn) throws Exception{
		
		VerificationToken token = tokenRepository.findByToken(tkn);
		if(token.getExpiredDateTime().isBefore(LocalDateTime.now())) {
			throw new Exception("Link expired");
		}

	}
	
	public VerificationToken findByName(String token) {
		return tokenRepository.findByToken(token);
	}
	
	
}
