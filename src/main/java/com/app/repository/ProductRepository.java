package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	public Product findByTitle(String title);	
}