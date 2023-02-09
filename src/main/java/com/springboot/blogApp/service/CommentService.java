package com.springboot.blogApp.service;

import java.util.List;
import com.springboot.blogApp.payload.CommentDto;

public interface CommentService {
	
	CommentDto createComment(long postId, CommentDto commentDto);
	
	List<CommentDto> getCommentsByPostId(long postId);
	
	CommentDto getCommentById(long postId, long commentId);

	CommentDto updateCommentById(long postId, long commentId, CommentDto commentDto);
	
	void deleteCommentById(long postId, long commentId);
	
}
