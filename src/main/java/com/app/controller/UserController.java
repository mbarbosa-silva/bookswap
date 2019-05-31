package com.app.controller;

import com.app.model.Ad;
import com.app.model.User;
import com.app.service.UserService;
import com.app.service.VerificationTokenService;
import com.app.service.SendGridMailService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private SendGridMailService mailService;
    
    @Autowired
    private VerificationTokenService tokenService;
    
    @GetMapping
    public List<User> getUsers(){
        return (List<User>) userService.findAll();
    }
    
	@GetMapping("/findAd")
	@ResponseBody
	@ApiOperation(value="Find all ads that belongs to a specific user", notes="Does query to retrive all the ads to belong to a specific user")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="List of products"),
			@ApiResponse(code=400, message="List could not be created")
	})
	public List<Ad> findAdByUser(Principal auth){
		try {
			return userService.findAdByUserName(auth.getName());
		} catch(Exception ex) {
			System.out.print("\nclass: UserController | method: findAdByUser \n" + ex.toString());
			return null;
		}
	}
    
    @PostMapping("/signup")
	@ApiOperation(value="Insert a new user", notes="Persist a new user in the database")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="User created"),
			@ApiResponse(code=400, message="User could not be created")
	})
    public void signUp(@RequestBody User newUser) {
    	newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
    	ServletUriComponentsBuilder.fromCurrentRequest();
    	String url = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
    	try {    	
    		var user = userService.createNewUser(newUser);
    		if(user == null) {
    			throw new Exception();
    		}
    		String token = tokenService.generateToken(user);
    		mailService.ConfirmAccountMail(user, token, url);

    	} catch(Exception ex) {
    		System.out.print("\nclass: UserController | method: SignUp \n" + ex.toString());
    	}
    }
    
    @GetMapping("/signup/confirm/{token}")
    public ResponseEntity<String> confirmMail(@PathVariable String token) {
    	
    	try {
    		tokenService.validateToken(token);
    		userService.validateUser(userService.findByUserName(tokenService.getTokenOwner(token)));
    	} catch(Exception ex) {
    		return ResponseEntity.ok().body("User not validate");
    	}
    	
    	return ResponseEntity.ok().body("User validate");
    }
    
}
