package com.app.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;

import com.app.controller.model.StdResponse;
import com.app.model.User;

public class Controller {

    protected void checkTokenOwnership(String username, Principal principal) throws Exception {
    	if(!principal.getName().equals(username)) {
    		throw new Exception("User does not own the token");
    	}
    }
	
}
