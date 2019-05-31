package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.Campus;

public interface CampusRepository extends JpaRepository<Campus, Long> {
	public Campus findByName(String name);
}