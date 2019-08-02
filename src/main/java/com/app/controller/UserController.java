 package com.app.controller;

import com.app.controller.model.StdResponse;
import com.app.model.Ad;
import com.app.model.Address;
import com.app.model.File;

import java.util.HashMap;
import java.util.LinkedHashMap;
import com.app.model.User;
import com.app.service.UserService;
import com.app.service.VerificationTokenService;
import com.google.gson.Gson;
import java.lang.reflect.Field;

import com.app.service.SendGridMailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.FileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
@RestController
@RequestMapping("/user")
public class UserController extends Controller {
    
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
    	
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestPart("user") User newUser,@RequestPart @Nullable MultipartFile file) {
    	newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
    	ServletUriComponentsBuilder.fromCurrentRequest();
    	String url = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
    	try {   
    		
    		var user = userService.createNewUser(newUser, file);
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
    		tokenService.deleteToken(token);
    		return ResponseEntity.ok().body(gson.toJson(new StdResponse("200", "-", "Confirmation sent", "/signup/confirm/{token}")));
    	} catch(Exception ex) {
    		return ResponseEntity.badRequest().body(gson.toJson(new StdResponse("400", ex.toString(), "Confirmation not sent", "/signup/confirm/{token}")));
    	}
    }
    
    @RequestMapping(value = "/update/{username}", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateUserInfo(@PathVariable String username, @RequestPart("user") Map<Object, Object> fields,@RequestPart @Nullable MultipartFile file, Principal principal){
    	try {
    		
    		checkTokenOwnership(username, principal);
    		var user = userService.findByUserName(username);
    		
    		userService.updateUser(user,file, fields);
    		
    		return ResponseEntity.ok().body(gson.toJson(new StdResponse("200", "-", "User updated", "/update/{username}")));
    	
    	} catch(Exception ex) {
    		return ResponseEntity.badRequest().body(gson.toJson(new StdResponse("400", ex.toString(), "User not updated", "/update/{username}")));
    	}

    }
    
    @RequestMapping(value = "/update/password/{username}", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateUserPassword(@PathVariable String username, @RequestBody HashMap<String, Object> password, Principal principal){       	
    	var user = userService.findByUserName(username); 
    	
    	try {
    		
    		checkTokenOwnership(username, principal);
    		String newPasswordEncoded = bCryptPasswordEncoder.encode(password.get("newPassword").toString());
    		String oldPasswordEncoded = bCryptPasswordEncoder.encode(password.get("oldPassword").toString());
    		
    		if(bCryptPasswordEncoder.matches(password.get("newPassword").toString(), user.getPassword())
    				&& !(bCryptPasswordEncoder.matches(oldPasswordEncoded, user.getPassword()))) {
    			throw new Exception("invalid password");
    		}
    		
    		user.setPassword(newPasswordEncoded);
    		userService.save(user);
    		
    		return ResponseEntity.ok().body(gson.toJson(new StdResponse("200", "-", "Password changed", "/update/password/{username}")));
    	
    	} catch(Exception ex) {
    		return ResponseEntity.badRequest().body(gson.toJson(new StdResponse("400", ex.toString(), "Password not changed", "/update/password/{username}")));
    	}
    }
    
    @RequestMapping(value = "/update/request/changepassword/{email}", method = RequestMethod.GET)
    public ResponseEntity<String> requestPasswordChange(@PathVariable String email) {
    	try { 
    		//var user = userService.findByUserName(username);
    		var user = userService.findByUserEmail(email);
    		
    		ServletUriComponentsBuilder.fromCurrentRequest();
    		String url = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
    	   	
    		String token = tokenService.generateToken(user);
    		
    		mailService.ResetPasswordMail(user, token, url);
    		return ResponseEntity.ok().body(gson.toJson(new StdResponse("200", "-", "Email sent to reset password", "/update/request/changepassword/{username}")));
    	
    	} catch(Exception ex) {
    		return ResponseEntity.badRequest().body(gson.toJson(new StdResponse("400", ex.toString(), "Could not send reset password Email", "/update/request/changepassword/{username}")));
    	}
    }
    
    @RequestMapping(value = "/update/request/changepassword/{email}/{token}", method = RequestMethod.GET)
    public ResponseEntity<String> confirmChangePasswordMail(@PathVariable String token,@PathVariable String email) {
    	try {
    		//checkTokenOwnership(username, principal);
    		//var user = userService.findByUserName(username);
    		var user = userService.findByUserEmail(email);
    		
    		String password = userService.resetPassword(user);	
    		
    		mailService.SendNewPassword(user, password);
    		tokenService.deleteToken(token);
    		
    		return ResponseEntity.ok().body(gson.toJson(new StdResponse("200", "-", "Reset password mail trigger", "/update/request/changepassword/{username}/{token}")));
    	} catch(Exception ex) {
    		return ResponseEntity.badRequest().body(gson.toJson(new StdResponse("400", ex.toString(), "Could not send password mail trigger", "/update/request/changepassword/{username}/{token}")));
    	}
    }
    
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String,Object>> findByUsername(@PathVariable String username,Principal principal){
    	try {

    		checkTokenOwnership(username, principal);
	    	var user = userService.findByUserName(username);
	    	File userPhoto;
	    	HashMap<String,Object> user_ = new HashMap<>();
	    	
	    	if(user.getPhoto()!=null) {
	    		userPhoto = user.getPhoto();
	    		user.setPhoto(null);
	    		user_.put("file", userPhoto.getData());
	    	}
	    	
	    	user_.put("user", user);
	    	//MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img));
	    	
	    	return ResponseEntity.ok()
            //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + userPhoto.getFileName() + "\"")
            //.header("File-type", userPhoto.getFileType())
            .body(user_);
	    	
    	} catch (Exception e) {
		
    		e.printStackTrace();
			return null;
		
    	}

    }
   
}
