package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
	public Comment findById(Long id);
}
