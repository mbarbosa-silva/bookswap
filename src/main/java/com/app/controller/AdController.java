package com.app.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.controller.model.StdResponse;
import com.app.model.Ad;
import com.app.model.Comment;
import com.app.model.File;
import com.app.model.User;
import com.app.service.AdService;
import com.app.service.UserService;
import com.google.gson.Gson;

@RestController
@RequestMapping("/ad")
public class AdController extends Controller {
	
	@Autowired
	private AdService adService;
	
	@Autowired
	private UserService userService;
	
	private static final Gson gson = new Gson();
	
	/*
	 * 
	 * 
	 * 
	 * 
	 */
	
	@GetMapping("/find/Bytitle/{title}")
	@ResponseBody
	public List<HashMap<String, Object>> findBytitle(@PathVariable String title){
		
		HashMap<String, Object> ad_;
		File photo;
		
		List<Ad> adListFromDB = adService.findAdByProductTitle(title);
		List<HashMap<String, Object>> adList = new ArrayList<>();
		
		for(var ad : adListFromDB){
			
			ad_ = new HashMap<>();
			if(ad.getProduct().getPhoto()!=null) {
				photo = ad.getProduct().getPhoto();
				ad_.put("file",photo.getData());
				ad.getProduct().setPhoto(null);
			}
			
			ad_.put("ad", ad);
			
			adList.add(ad_);
		}
		
		return adList;
	}
	
	@GetMapping("/find/example")
	@ResponseBody
	public List<HashMap<String, Object>> findAdByExample(@RequestBody Ad ad){
		
		HashMap<String, Object> ad_;
		File photo;
		
		List<Ad> adListFromDB = adService.findAdByExample(ad);
		List<HashMap<String, Object>> adList = new ArrayList<>();
		
		for(var ad_i : adListFromDB){
			
			ad_ = new HashMap<>();
			if(ad_i.getProduct().getPhoto()!=null) {
				photo = ad_i.getProduct().getPhoto();
				ad_.put("file",photo.getData());
				ad_i.getProduct().setPhoto(null);
			}
			
			ad_.put("ad", ad_i);
			
			adList.add(ad_);
		}
		
		return adList;
		
	}
	
	@GetMapping("/find/{username}")
	@ResponseBody
	public List<HashMap<String, Object>> findAdByUsername(@PathVariable String username,Principal principal){
		try {
			
			checkTokenOwnership(username,principal);
			HashMap<String, Object> ad_;
			File photo;
			
			List<Ad> adListFromDB = adService.findAdByUserName(username);
			List<HashMap<String, Object>> adList = new ArrayList<>();
			
			for(var ad_i : adListFromDB){
				
				ad_ = new HashMap<>();
				if(ad_i.getProduct().getPhoto()!=null) {
					photo = ad_i.getProduct().getPhoto();
					ad_.put("file",photo.getData());
					ad_i.getProduct().setPhoto(null);
				}
				
				ad_.put("ad", ad_i);
				
				adList.add(ad_);
			}
			
			return adList;

		} catch(Exception ex) {	
			System.out.print("\nclass: UserController | method: findAdByUser \n" + ex.toString());
			return null;
		}
	}
	
	@PostMapping("/create/{username}")
	@ResponseBody
	@Transactional
	public Ad createNewAd(@RequestPart("ad") Ad ad,@RequestPart @Nullable MultipartFile file, @PathVariable String username, Principal principal) {
		try {
			checkTokenOwnership(username,principal);
			User user = userService.findByUserName(username);			
			return adService.createNewAd(ad, file, user);
		} catch (Exception ex) {
			System.out.print("\nclass: AdController | method: createNewAd \n" + ex.toString());
			return null;
		}
	}
	
	@PatchMapping("/edit/{id}/{username}")
	@ResponseBody
	public HashMap<String, Object> editAd(@PathVariable String id,@RequestPart("ad") Map<Object, Object> fields,@RequestPart @Nullable MultipartFile file, @PathVariable String username, Principal principal) {
		try {
			checkTokenOwnership(username, principal);
			User user = userService.findByUserName(username);
			adService.checkIfAdBelongsToUser(id, user);
			
			HashMap<String, Object> ad = new HashMap<>();
			
			var ad_ = adService.updateAd(id,file, fields);
			var photo_ = ad_.getProduct().getPhoto().getData();
			ad_.getProduct().setPhoto(null);
			
			ad.put("ad", ad_);
			ad.put("file",photo_);
			
			return ad;
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	@DeleteMapping("/delete/{id}/{username}")
	public ResponseEntity<String> deleteAd(@PathVariable String id, @PathVariable String username, Principal principal){
		try {
			checkTokenOwnership(username, principal);
			User user = userService.findByUserName(username);
			adService.checkIfAdBelongsToUser(id, user);
			adService.delete(adService.findAdById(id));
			
			return ResponseEntity.ok().body(gson.toJson(new StdResponse("200", "-", "Ad successful deleted", "/delete/{id}/{username}")));
		} catch(Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping("/comment/create/{id}/{username}")
	public ResponseEntity<String> createComment(@RequestBody Comment newComment,@PathVariable String id, @PathVariable String username, Principal principal){
		try{
			User user = userService.findByUserName(username);
			checkTokenOwnership(username, principal);
		
			adService.createNewComment(id, user, newComment);
			
			return ResponseEntity.ok().body(gson.toJson(new StdResponse("200","no error", "Comment created", "/comment/create/{id}/{username}")));
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().body(gson.toJson(new StdResponse("400", ex.toString(), "Could not create comment", "/comment/create/{id}/{username}")));
		}
		
	}
	
	@PostMapping("/comment/edit/{username}")
	public ResponseEntity<String> editComment(@RequestBody HashMap<String,String> newComment, @PathVariable String username, Principal principal){
		try{
			checkTokenOwnership(username, principal);
			adService.editComment(newComment);
			
			return ResponseEntity.ok().body(gson.toJson(new StdResponse("200","no error", "Comment created", "/comment/create/{id}/{username}")));
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().body(gson.toJson(new StdResponse("400", ex.toString(), "Could not create comment", "/comment/create/{id}/{username}")));
		}
		
	}
	
	
}
