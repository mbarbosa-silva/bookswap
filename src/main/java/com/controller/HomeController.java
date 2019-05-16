package com.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Valid
@RestController
@RequestMapping("/")
public class HomeController {

	@GetMapping
	ResponseEntity<String> hello() {
	    return ResponseEntity.ok("Hello World!");
	}
	
}
