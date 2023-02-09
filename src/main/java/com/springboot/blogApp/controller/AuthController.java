package com.springboot.blogApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blogApp.payload.JwtAuthResponse;
import com.springboot.blogApp.payload.LoginDto;
import com.springboot.blogApp.payload.RegisterDto;
import com.springboot.blogApp.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	private AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	//Building Login REST API
	@PostMapping(value = {"/login", "/signin"})
	public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
		String token = authService.login(loginDto);
		
		JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
		jwtAuthResponse.setAccessToken(token);
		
		return ResponseEntity.ok(jwtAuthResponse);
	}
	
	//Building Register REST API
	@PostMapping(value = {"/register", "/signup"})
	public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
		String response = authService.register(registerDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
}
