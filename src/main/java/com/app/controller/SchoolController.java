package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.Campus;
import com.app.model.School;
import com.app.service.SchoolService;

@RestController
@RequestMapping("/school")
public class SchoolController extends Controller {

	@Autowired
	private SchoolService schoolService;
	
	@GetMapping("/find")
	@ResponseBody
	public List<School> findAllSchool(){
		try {
			return schoolService.findAllSchool();
		} catch(Exception ex) {	
			ex.printStackTrace();
			return null;
		}
	}
	
	@GetMapping("/campus/find")
	@ResponseBody
	public List<Campus> findAllCampus(){
		try {
			return schoolService.findAllCampus();
		} catch(Exception ex) {	
			ex.printStackTrace();
			return null;
		}
	}
	
}
