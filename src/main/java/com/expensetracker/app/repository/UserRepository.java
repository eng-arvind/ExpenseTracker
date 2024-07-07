package com.expensetracker.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expensetracker.app.entity.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Integer> {
	
	Boolean existsByEmail(String email);
	
	Optional<User> findByEmail(String email);

}
 