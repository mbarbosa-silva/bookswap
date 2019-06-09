package com.app.controller;

import com.app.controller.model.StdResponse;
import com.app.model.Ad;
import com.app.model.User;
import com.app.service.UserService;
import com.app.service.VerificationTokenService;
import com.google.gson.Gson;
import java.lang.reflect.Field;
import com.app.service.SendGridMailService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private SendGridMailService mailService;
    
    @Autowired
    private VerificationTokenService tokenService;
    
    private static final Gson gson = new Gson();
    
    @GetMapping
    public List<User> getUsers(){
        return (List<User>) userService.findAll();
    }
    
	@GetMapping("/findAd")
	@ResponseBody
	public List<Ad> findAdByUser(Principal auth){
		try {
			return userService.findAdByUserName(auth.getName());
		} catch(Exception ex) {
			System.out.print("\nclass: UserController | method: findAdByUser \n" + ex.toString());
			return null;
		}
	}
    	
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User newUser) {
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
    		return ResponseEntity.ok().body(gson.toJson(new StdResponse("200", "No error", "User created", "user/signup")));
    	} catch(Exception ex) {
    		System.out.print("\nclass: UserController | method: SignUp \n" + ex.toString());
    		return ResponseEntity.badRequest().body(gson.toJson(new StdResponse("400", ex.toString(), "User not created", "user/signup")));
    	}
    }
    
    @GetMapping("/signup/confirm/{token}")
    public ResponseEntity<String> confirmMail(@PathVariable String token) {
    	
    	try {
    		tokenService.validateToken(token);
    		userService.validateUser(userService.findByUserName(tokenService.getTokenOwner(token)));
    		/**
    		 * TODO: delete token after its use
    		 */
    		return ResponseEntity.ok().body(gson.toJson(new StdResponse("200", "-", "Confirmation sent", "/signup/confirm/{token}")));
    	} catch(Exception ex) {
    		return ResponseEntity.badRequest().body(gson.toJson(new StdResponse("400", ex.toString(), "Confirmation not sent", "/signup/confirm/{token}")));
    	}
    	
    }
    
    @RequestMapping(value = "/update/{username}", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateUserInfo(@PathVariable String username, @RequestBody Map<Object, Object> fields){
    	try {
    		var user = userService.findByUserName(username);
    		fields.forEach((k, v) -> {
    			Field field = ReflectionUtils.findField(User.class, (String) k);
    			field.setAccessible(true);
        	ReflectionUtils.setField(field, user, v);
    		});  	
    		userService.save(user);
    		return ResponseEntity.ok().body(gson.toJson(new StdResponse("200", "-", "User updated", "/update/{username}")));
    	} catch(Exception ex) {
    		return ResponseEntity.badRequest().body(gson.toJson(new StdResponse("400", ex.toString(), "User not updated", "/update/{username}")));
    	}

    }
    
    @RequestMapping(value = "/update/password/{username}", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateUserPassword(@PathVariable String username, @RequestBody String newPassword){       	
    	var user = userService.findByUserName(username); 
    	try {
    		String newPasswordEncoded = bCryptPasswordEncoder.encode(newPassword);
    		if(bCryptPasswordEncoder.matches(newPassword, user.getPassword())) {
    			throw new Exception("invalid password");
    		}
    		user.setPassword(newPasswordEncoded);
    		userService.save(user);
    		return ResponseEntity.ok().body(gson.toJson(new StdResponse("200", "-", "Password changed", "/update/password/{username}")));
    	} catch(Exception ex) {
    		return ResponseEntity.badRequest().body(gson.toJson(new StdResponse("400", ex.toString(), "Password not changed", "/update/password/{username}")));
    	}
    }
    
    @RequestMapping(value = "/update/request/changepassword/{username}", method = RequestMethod.GET)
    public ResponseEntity<String> requestPasswordChange(@PathVariable String username) {
    	var user = userService.findByUserName(username);
    	ServletUriComponentsBuilder.fromCurrentRequest();
    	String url = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
    	try {    	
    		String token = tokenService.generateToken(user);
    		mailService.ResetPasswordMail(user, token, url);
    		/**
    		 * TODO: delete token after its use
    		 */
    		return ResponseEntity.ok().body(gson.toJson(new StdResponse("200", "-", "Email sent to reset password", "/update/request/changepassword/{username}")));
    	} catch(Exception ex) {
    		return ResponseEntity.badRequest().body(gson.toJson(new StdResponse("400", ex.toString(), "Could not send reset password Email", "/update/request/changepassword/{username}")));
    	}
    }
    
    @RequestMapping(value = "/update/request/changepassword/{username}/{token}", method = RequestMethod.GET)
    public ResponseEntity<String> confirmChangePasswordMail(@PathVariable String token,@PathVariable String username) {
    	try {    	
    		var user = userService.findByUserName(username);
    		String password = userService.resetPassword(user);	
    		mailService.SendNewPassword(user, password);
    		/**
    		 * TODO: delete token after its use
    		 */
    		return ResponseEntity.ok().body(gson.toJson(new StdResponse("200", "-", "Reset password mail trigger", "/update/request/changepassword/{username}/{token}")));
    	} catch(Exception ex) {
    		return ResponseEntity.badRequest().body(gson.toJson(new StdResponse("400", ex.toString(), "Could not send password mail trigger", "/update/request/changepassword/{username}/{token}")));
    	}
    }
   
}
