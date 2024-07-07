package com.expensetracker.app.service;

import com.expensetracker.app.entity.User;
import com.expensetracker.app.entity.UserModel;

public interface UserService {
	
	User createUser(UserModel user);
	
	User readUser();
	
	User updateUser(UserModel user);
	
	void deleteUser();
	
	User getLoggedInUser();

}
