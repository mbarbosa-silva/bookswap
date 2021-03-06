package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.model.Ad;
import com.app.model.File;

@Repository
public interface FileRepository extends JpaRepository<File, String>{
	public File findFileByProductId(Long id);
	public File findFileByUserId(Long id);
}
