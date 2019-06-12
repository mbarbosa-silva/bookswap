package com.app.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.model.User;
import com.app.model.VerificationToken;
import com.app.repository.VerificationTokenRepository;

@Service
public class VerificationTokenService {

	@Autowired
	private VerificationTokenRepository tokenRepository;
	
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
		try {
			VerificationToken token = findByName(tkn);
			if(token.getExpiredDateTime().isBefore(LocalDateTime.now())) {
				throw new Exception("Link expired");
			}
			tokenRepository.save(token);
		} catch(Exception ex) {
			System.out.print("\nclass: VerificationTokenService | method: validateToken \n" + ex.toString());
			throw ex;
		}
	}
	
	public void deleteToken(String tkn) {
		VerificationToken token = findByName(tkn);
		token.setToken(null);
		tokenRepository.save(token);
	}
	
	public String getTokenOwner(String token) {
		return findByName(token).getUser().getUsername();
	}

	public VerificationToken findByName(String token) {
		return tokenRepository.findByToken(token);
	}
	
}
