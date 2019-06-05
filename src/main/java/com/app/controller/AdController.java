package com.app.controller;

import java.security.Principal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.Ad;
import com.app.model.User;
import com.app.service.AdService;
import com.app.service.UserService;

@RestController
@RequestMapping("/ad")
public class AdController {
	
	@Autowired
	private AdService adService;
	
	@Autowired
	private UserService userService;

	@GetMapping("findBytitle/{title}")
	@ResponseBody
	public Ad findBytitle(@PathVariable String title){
		return adService.findAdByProductTitle(title);
	}
	
	@PostMapping("/findbyexample")
	@ResponseBody
	public List<Ad> findAdByExample(@RequestBody Ad ad){
		return adService.findAdByExample(ad);
	}
	
	@PostMapping("/newad")
	@ResponseBody
	@Transactional
	public Ad createNewAd(@RequestBody Ad ad, Principal auth) {
		try {
			User user = userService.findByUserName(auth.getName());			
			ad.setUser(user);
			user.addAd(ad);
			return adService.save(ad);
		} catch (Exception ex) {
			System.out.print("\nclass: AdController | method: createNewAd \n" + ex.toString());
			return null;
		}
	}
	
}
