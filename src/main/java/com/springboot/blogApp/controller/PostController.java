package com.springboot.blogApp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blogApp.payload.PostDto;
import com.springboot.blogApp.payload.PostResponse;
import com.springboot.blogApp.service.PostService;
import com.springboot.blogApp.utils.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
	
	//Implementing CI
	private PostService postService;  //loose coupling by using interface

	//@Autowired annotation is omitted as only 1 Constructor
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	//Create Blog Post REST Api
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
		return new ResponseEntity<>(postService.createPost(postDto),HttpStatus.CREATED);
	}
	
	//Get all Posts REST Api
	@GetMapping
	public PostResponse getAllPosts(
		@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
		@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
		@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
		@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
	){
		return postService.getAllPosts(pageNo,pageSize,sortBy,sortDir);
	}
	
	//Get Post by Id REST Api
	@GetMapping("/{id}")
	public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") long id){
		return ResponseEntity.ok(postService.getPostById(id));
	}
	
	//Update Post by Id REST Api
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable(name = "id") long id){
		PostDto updateResponse = postService.updatePostById(postDto, id);
		return new ResponseEntity<>(updateResponse,HttpStatus.OK);
	}
	
	//Delete Post by Id REST Api
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id){
			postService.deletePostById(id);
			return new ResponseEntity<>("Post Entity deleted successfully!",HttpStatus.OK);
		}
	
	//Build Get Posts by Category REST Api
	@GetMapping("/category/{id}")
	public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable(name = "id") Long categoryId){
		List<PostDto> postDtos = postService.getPostsByCategory(categoryId);
		return ResponseEntity.ok(postDtos);
	}

}
