package com.app.main.security;

import com.app.main.security.model.AccountCredentials;
import com.app.main.security.model.LoginResponseBody;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	private Gson gson = new Gson();
	
	protected JWTLoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		AccountCredentials credentials = new ObjectMapper()
				.readValue(request.getInputStream(), AccountCredentials.class);
		
		return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
						credentials.getUsername(), 
						credentials.getPassword(), 
						Collections.emptyList()
						)
				);
	}
	
	private HttpServletResponse SetResponseJsonBody(HttpServletResponse response
			,String status, String error, String message, String path) throws IOException {
				
		LoginResponseBody responseBody = new LoginResponseBody(status, error, message, path);
		String body = this.gson.toJson(responseBody);
		
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(body);
        out.flush();
        
        return response;
        
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
			Authentication auth) throws IOException, ServletException {
		
		response.setStatus(200);
		TokenAuthentication.addAuthentication(response, auth.getName());
		this.SetResponseJsonBody(response,"200","-","succeful login","user/login");

	}
	
	@Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
		
		response.setStatus(401);
		this.SetResponseJsonBody(response,"400","-","password or username incorrect","user/login");
    }

}