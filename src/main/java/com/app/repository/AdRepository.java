package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.model.Ad;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
	public List<Ad> findByProductTitleIgnoreCaseContaining(String title);
	public Ad findAdById(Long Id);
}
