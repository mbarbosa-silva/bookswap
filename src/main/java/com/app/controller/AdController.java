package com.app.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.controller.model.StdResponse;
import com.app.model.Ad;
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
	
	@GetMapping("/find/Bytitle/{title}")
	@ResponseBody
	public List<Ad> findBytitle(@PathVariable String title){
		return adService.findAdByProductTitle(title);
	}
	
	@GetMapping("/find/example")
	@ResponseBody
	public List<Ad> findAdByExample(@RequestBody Ad ad){
		return adService.findAdByExample(ad);
	}
	
	@PostMapping("/create/{username}")
	@ResponseBody
	@Transactional
	public Ad createNewAd(@RequestBody Ad ad, @PathVariable String username, Principal principal) {
		try {
			checkTokenOwnership(username,principal);
			User user = userService.findByUserName(username);			
			ad.setUser(user);
			user.addAd(ad);
			return adService.save(ad);
		} catch (Exception ex) {
			System.out.print("\nclass: AdController | method: createNewAd \n" + ex.toString());
			return null;
		}
	}
	
	@GetMapping("/find/{username}")
	@ResponseBody
	public List<Ad> findAdByUsername(@PathVariable String username,Principal principal){
		try {
			checkTokenOwnership(username,principal);
			return adService.findAdByUserName(username);
		} catch(Exception ex) {	
			System.out.print("\nclass: UserController | method: findAdByUser \n" + ex.toString());
			return null;
		}
	}
	
	@PatchMapping("/edit/{id}/{username}")
	@ResponseBody
	public ResponseEntity<Ad> editAd(@PathVariable String id, @PathVariable String username,@RequestBody Map<Object, Object> fields, Principal principal) {
		try {
			checkTokenOwnership(username, principal);
			User user = userService.findByUserName(username);
			adService.checkIfAdBelongsToUser(id, user);
			adService.updateAd(id, fields);
			
			return ResponseEntity.ok().body(adService.findAdById(id));
		} catch(Exception ex) {
			return ResponseEntity.badRequest().build();
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
	
}
