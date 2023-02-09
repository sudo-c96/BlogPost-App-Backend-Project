package com.springboot.blogApp.service;

import com.springboot.blogApp.payload.LoginDto;
import com.springboot.blogApp.payload.RegisterDto;

public interface AuthService {
	
	String login(LoginDto loginDto);
	
	String register(RegisterDto registerDto);

}
