package com.expensetracker.app.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.expensetracker.app.entity.User;
import com.expensetracker.app.entity.UserModel;
import com.expensetracker.app.exceptions.ItemAlreadyExistsException;
import com.expensetracker.app.exceptions.ResourceNotFoundException;
import com.expensetracker.app.repository.UserRepository;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User createUser(UserModel user) {
		
		if(userRepository.existsByEmail(user.getEmail())) {
			throw new ItemAlreadyExistsException("User is already register with email:"+ user.getEmail());
		}
		User newUser = new User();
		BeanUtils.copyProperties(user, newUser);
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		return userRepository.save(newUser);
	}  

	@Override
	public User readUser() {
		int userId = getLoggedInUser().getId();
		return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for the id: "+ userId));
	}

	@Override
	public User updateUser(UserModel user) {
		
		User existingUser = readUser();
		existingUser.setName(user.getName() != null ?  user.getName() : existingUser.getName());
		existingUser.setEmail(user.getEmail() != null ?  user.getEmail() : existingUser.getEmail());
		existingUser.setPassword(user.getPassword() != null ? passwordEncoder.encode(user.getPassword()) : existingUser.getPassword());
		existingUser.setAge(user.getAge() != null ?  user.getAge() : existingUser.getAge());
		userRepository.save(existingUser);
		return existingUser;
	}

	@Override
	public void deleteUser() {
		User existingUser = readUser();
		userRepository.delete(existingUser);
	}

	@Override
	public User getLoggedInUser() {
		Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found for the email:"+email));
		
	}
	

}
