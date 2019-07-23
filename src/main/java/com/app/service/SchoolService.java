package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.repository.SchoolRepository;
import com.app.model.Campus;
import com.app.model.School;
import com.app.repository.CampusRepository;

@Service
public class SchoolService {

	@Autowired
	private SchoolRepository schoolRepository;
	
	@Autowired
	private CampusRepository campusRepository;
	
	public List<School> findAllSchool(){
		return schoolRepository.findAll();
	}
	
	public List<Campus> findAllCampus(){
		return campusRepository.findAll();
	}
}
