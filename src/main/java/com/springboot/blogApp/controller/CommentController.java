package com.springboot.blogApp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blogApp.payload.CommentDto;
import com.springboot.blogApp.service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class CommentController {
	
	//Implementing CI
	private CommentService commentService;   //loose coupling by using interface

	//@Autowired annotation is omitted as only 1 Constructor
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}
		
	//Create Comment for a postId
	@PostMapping("/posts/{postId}/comments")
	public ResponseEntity<CommentDto> createComment(@PathVariable(name = "postId") long postId, @Valid @RequestBody CommentDto commentDto){
		return new ResponseEntity<>(commentService.createComment(postId,commentDto),HttpStatus.CREATED);
	}
	
	//Get all Comments for a postId
	@GetMapping("/posts/{postId}/comments")
	public List<CommentDto> getCommentsByPostId(@PathVariable(name = "postId") long postId){
		return commentService.getCommentsByPostId(postId);
	}
	
	//Get a comment for a postId
	@GetMapping("/posts/{postId}/comments/{commentId}")
	public ResponseEntity<CommentDto> getCommentById(@PathVariable(name = "postId") long postId,
			@PathVariable(name = "commentId") long commentId){
		
		CommentDto commentDto = commentService.getCommentById(postId, commentId);
		return new ResponseEntity<>(commentDto, HttpStatus.OK);
	}
	
	//Update a comment for a postId
	@PutMapping("/posts/{postId}/comments/{commentId}")
	public ResponseEntity<CommentDto> updateCommentById(@PathVariable(name = "postId") long postId,
				@PathVariable(name = "commentId") long commentId, 
				@Valid @RequestBody CommentDto commentDto){
			
		CommentDto updatedComment = commentService.updateCommentById(postId, commentId, commentDto);
		return new ResponseEntity<>(updatedComment, HttpStatus.OK);
	}
	
	//Delete a comment for a postId
	@DeleteMapping("/posts/{postId}/comments/{commentId}")
	public ResponseEntity<String> deleteCommentById(@PathVariable(name = "postId") long postId,
				@PathVariable(name = "commentId") long commentId){
			
		commentService.deleteCommentById(postId, commentId);
		return new ResponseEntity<>("Comment deleted successfully!", HttpStatus.OK);
	}
	
}
