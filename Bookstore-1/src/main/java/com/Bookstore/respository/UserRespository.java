package com.Bookstore.respository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Bookstore.model.User;

public interface UserRespository extends JpaRepository<User, Long> {
	
	User findByUsername(String username);
	User findByEmail(String email);
	//void createPasswordResetTokenForUser(final User user, final String token);

}
