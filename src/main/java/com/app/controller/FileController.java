package com.app.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.app.controller.model.StdResponse;
import com.app.model.File;
import com.app.service.FileService;
import com.app.service.UserService;
import com.google.gson.Gson;

@RestController
@RequestMapping("/file")
public class FileController extends Controller {
	
	private static final Gson gson = new Gson();
	
    @Autowired
    private FileService fileService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/userPhoto")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam String username, Principal principal) throws Exception {
    	try {
    		checkTokenOwnership(username, principal);
    		File File = fileService.storeFile(file);
    		userService.setUserPhoto(File, username);
    		return ResponseEntity.ok().body(gson.toJson(new StdResponse("200", "No error", "User created", "user/signup")));
        } catch(Exception ex) {
        	System.out.print("\nclass: UserController | method: SignUp \n" + ex.toString());
    		return ResponseEntity.badRequest().body(gson.toJson(new StdResponse("400", ex.toString(), "User not created", "user/signup")));
    	
        }
    }

}
