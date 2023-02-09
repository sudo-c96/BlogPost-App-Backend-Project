package com.springboot.blogApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.blogApp.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByPostId(long postId);
	
}
