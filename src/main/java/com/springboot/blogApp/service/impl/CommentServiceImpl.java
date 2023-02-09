package com.springboot.blogApp.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.blogApp.entity.Comment;
import com.springboot.blogApp.entity.Post;
import com.springboot.blogApp.exception.BlogApiException;
import com.springboot.blogApp.exception.ResourceNotFoundException;
import com.springboot.blogApp.payload.CommentDto;
import com.springboot.blogApp.repository.CommentRepository;
import com.springboot.blogApp.repository.PostRepository;
import com.springboot.blogApp.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	//Implementing CI
	private CommentRepository commentRepository;
	private PostRepository postRepository;
	
	private ModelMapper modelMapper;
		
	//@Autowired annotation is omitted as only 1 Constructor
	public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.modelMapper = modelMapper;
	}
	
	//Converting DTO to Entity using modelMapper
	private Comment mapToEntity(CommentDto commentDto) {	
		Comment comment = modelMapper.map(commentDto, Comment.class);
		return comment;
	}
		
	//Converting Entity to DTO using modelMapper
	private CommentDto mapToDTO(Comment comment) {			
		CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
		return commentDto;
	}
	
	@Override
	public CommentDto createComment(long postId, CommentDto commentDto) {
		
		//Convert DTO to Entity
		Comment comment = mapToEntity(commentDto);
		
		//Retrieve Post Entity by Id
		Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));
		
		//Set Post Entity to Comment Entity
		comment.setPost(post);
		
		//Comment Entity saving to DB
		Comment newComment = commentRepository.save(comment);
		
		//Convert Entity to DTO
		CommentDto commentResponse = mapToDTO(newComment);
		
		return commentResponse;
				
	}

	@Override
	public List<CommentDto> getCommentsByPostId(long postId) {
		
		//Retrieve Comments by postId
		List<Comment> comments = commentRepository.findByPostId(postId);
		
		//Convert list of Comments to list of CommentDto's
		return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
				
	}

	@Override
	public CommentDto getCommentById(long postId, long commentId) {
		
		//Retrieve Post Entity by Id
		Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));
		
		//Retrieve Comment Entity by Id
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","id",commentId));
		
		if(!comment.getPost().getId().equals(post.getId())) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post!");
		}
		
		//Convert Entity to DTO
		CommentDto commentResponse = mapToDTO(comment);
		
		return commentResponse;
		
	}

	@Override
	public CommentDto updateCommentById(long postId, long commentId, CommentDto commentDto) {
		
		//Retrieve Post Entity by Id
		Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));
		
		//Retrieve Comment Entity by Id
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","id",commentId));
		
		if(!comment.getPost().getId().equals(post.getId())) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post!");
		}
		
		//Convert DTO to Entity
		comment.setName(commentDto.getName());
		comment.setEmail(commentDto.getEmail());
		comment.setBody(commentDto.getBody());
		
		//Updated Comment Entity saving to DB
		Comment updatedComment = commentRepository.save(comment);
		
		//Convert Entity to DTO
		CommentDto updatedResponse = mapToDTO(updatedComment);
		
		return updatedResponse;
		
	}

	@Override
	public void deleteCommentById(long postId, long commentId) {
		
		//Retrieve Post Entity by Id
		Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));
				
		//Retrieve Comment Entity by Id
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","id",commentId));
				
		if(!comment.getPost().getId().equals(post.getId())) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post!");
		}
		
		commentRepository.delete(comment);
		
	}

}
