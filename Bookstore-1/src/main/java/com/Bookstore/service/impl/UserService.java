package com.Bookstore.service.impl;

import java.util.Set;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.Bookstore.model.User;
import com.Bookstore.model.UserBilling;
import com.Bookstore.model.UserPayment;
import com.Bookstore.model.UserShipping;
import com.Bookstore.security.PasswordResetToken;

public interface UserService extends UserDetailsService {

	User findByUsername(String username);
	
	User findByEmail(String email);
	
	PasswordResetToken getPasswordResetToken(final String token);

	void createPasswordResetTokenForUser(final User user, final String token);
	
	//User createUser(User user, Set<UserRole> userRoles) throws Exception;
	
	User save(User user) ;
	
	void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);
		
	void setUserDefaultPayment(Long userPaymentId, User user);
	
	void UpdateUserShipping(UserShipping userShipping, User user);
	
	void setUserDefaultShipping(Long userShippingId, User user);
	
	User findById(Long id);
}
