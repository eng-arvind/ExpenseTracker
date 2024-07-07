package com.expensetracker.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.expensetracker.app.entity.AuthModel;
import com.expensetracker.app.entity.JwtResponse;
import com.expensetracker.app.entity.User;
import com.expensetracker.app.entity.UserModel;
import com.expensetracker.app.security.CustomUserDetailsService;
import com.expensetracker.app.service.UserService;
import com.expensetracker.app.util.JwtTokenUtil;

import jakarta.validation.Valid;

@RestController
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody AuthModel authModel) throws Exception {
		authenticate(authModel.getEmail(),authModel.getPassword());
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authModel.getEmail());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return new ResponseEntity<JwtResponse>(new JwtResponse(token), HttpStatus.OK);
	}
	
	private void authenticate(String email, String password) throws Exception {
		try {
			
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			
		}
		catch(DisabledException e) {
			
			throw new Exception("User disabled");
			
		}
		
		catch(BadCredentialsException e) {
			throw new Exception("Bad credentials");
		}
		
	}

	@PostMapping("/register")
	public ResponseEntity<User> save(@Valid @RequestBody UserModel user) {
		
		return new ResponseEntity<User>(userService.createUser(user), HttpStatus.CREATED);
	}
	

}
