package com.app.controller;

import java.security.Principal;

public class Controller {

    protected void checkTokenOwnership(String username, Principal principal) throws Exception {
    	if(!principal.getName().equals(username)) {
    		throw new Exception("User does not own the token");
    	}
    }
	
}
