package com.springboot.blogApp.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.blogApp.entity.Category;
import com.springboot.blogApp.entity.Post;
import com.springboot.blogApp.exception.ResourceNotFoundException;
import com.springboot.blogApp.payload.PostDto;
import com.springboot.blogApp.payload.PostResponse;
import com.springboot.blogApp.repository.CategoryRepository;
import com.springboot.blogApp.repository.PostRepository;
import com.springboot.blogApp.service.PostService;

@Service
public class PostServiceImpl implements PostService {
	
	//Implementing CI
	private PostRepository postRepository;
	
	private ModelMapper modelMapper;
	
	private CategoryRepository categoryRepository;
	
	//@Autowired annotation is omitted as only 1 Constructor
	public PostServiceImpl(PostRepository postRepository, 
							ModelMapper modelMapper, 
							CategoryRepository categoryRepository) {
		this.postRepository = postRepository;
		this.modelMapper = modelMapper;
		this.categoryRepository = categoryRepository;
	}

	
	@Override
	public PostDto createPost(PostDto postDto) {
		
		Category category = categoryRepository.findById(postDto.getCategoryId())
							.orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));
		
		//Convert DTO to Entity
		Post post = mapToEntity(postDto);
		post.setCategory(category);
		Post newPost = postRepository.save(post);
		//Convert Entity to DTO
		PostDto postResponse = mapToDTO(newPost);
		return postResponse;
		
	}

	@Override
	public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
		
		//Sorting dynamically based on input
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		
		//Create Pageable instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		
		//Page Object created
		Page<Post> posts = postRepository.findAll(pageable);
		
		//Get Content for Page Object
		List<Post> listOfPosts = posts.getContent();
		
		//Pass to stream and take result in a list
		List<PostDto> content = listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
		
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(content);
		postResponse.setPageNo(posts.getNumber());
		postResponse.setPageSize(posts.getSize());
		postResponse.setTotalElements(posts.getTotalElements());
		postResponse.setTotalPages(posts.getTotalPages());
		postResponse.setLast(posts.isLast());
		
		return postResponse;
		
	}
	
	@Override
	public PostDto getPostById(long id) {
		
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
		return mapToDTO(post);
		
	}
	
	@Override
	public PostDto updatePostById(PostDto postDto, long id) {

		//Get Post by Id from DB or else throw exception
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
		
		//Get Category by Id from DB or else throw exception
		Category category = categoryRepository.findById(postDto.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));
		
		//Set the postDto values to that Post retrieved
		post.setTitle(postDto.getTitle());
		post.setDescription(postDto.getDescription());
		post.setContent(postDto.getContent());
		post.setCategory(category);
		
		Post updatedPost = postRepository.save(post);
		return mapToDTO(updatedPost);
		
	}
	
	@Override
	public void deletePostById(long id) {
		
		//Get Post by Id from DB or else throw exception
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
		//Delete the retrieved Post
		postRepository.delete(post);
		
	}
	
	@Override
	public List<PostDto> getPostsByCategory(Long categoryId) {
		
		//Get Category by Id from DB or else throw exception
		Category category = categoryRepository.findById(categoryId)
						.orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
		
		List<Post> posts = postRepository.findByCategoryId(categoryId);
		
		return posts.stream().map((post) -> mapToDTO(post))
				.collect(Collectors.toList());
		
	}
	
	//Converting DTO to Entity using modelMapper
	private Post mapToEntity(PostDto postDto) {
		Post post = modelMapper.map(postDto, Post.class);
		return post;	
	}
	
	//Converting Entity to DTO using modelMapper
	private PostDto mapToDTO(Post post) {
		PostDto postDto = modelMapper.map(post, PostDto.class);
		return postDto;
	}

}
