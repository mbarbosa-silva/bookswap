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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/ad")
public class AdController {
	
	@Autowired
	private AdService adService;
	
	@Autowired
	private UserService userService;

	@GetMapping("findBytitle/{title}")
	@ResponseBody
	@ApiOperation(value="Find ad by product name", notes="Query all ads that contains a book entity with a specific name")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="List of products"),
			@ApiResponse(code=400, message="List could not be created")
	})
	public Ad findBytitle(@PathVariable String title){
		return adService.findAdByProductTitle(title);
	}
	
	@PostMapping("/findbyexample")
	@ResponseBody
	@ApiOperation(value="Find ad that matchs with the ad example", notes="Does a query-by-example operation to get the specific ads")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="List of products"),
			@ApiResponse(code=400, message="List could not be created")
	})
	public List<Ad> findAdByExample(@RequestBody Ad ad){
		return adService.findAdByExample(ad);
	}
	
	@PostMapping("/newad")
	@ResponseBody
	@ApiOperation(value="Create a new ad", notes="Does a query-by-example operation to get the specific ads")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Ad succefully created"),
			@ApiResponse(code=400, message="Ad not created")
	})
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
