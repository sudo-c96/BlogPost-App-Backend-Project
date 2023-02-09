package com.springboot.blogApp.service;

import java.util.List;

import com.springboot.blogApp.payload.PostDto;
import com.springboot.blogApp.payload.PostResponse;

public interface PostService {
	
	PostDto createPost(PostDto postDto);

	PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
	
	PostDto getPostById(long id);
	
	PostDto updatePostById(PostDto postDto, long id);
	
	void deletePostById(long id);
	
	List<PostDto> getPostsByCategory(Long categoryId);
	
}
