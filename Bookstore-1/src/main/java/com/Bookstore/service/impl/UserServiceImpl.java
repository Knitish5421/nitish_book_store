package com.Bookstore.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Bookstore.model.Role;
import com.Bookstore.model.ShoppingCart;
import com.Bookstore.model.User;
import com.Bookstore.model.UserBilling;
import com.Bookstore.model.UserPayment;
import com.Bookstore.model.UserShipping;
import com.Bookstore.respository.PasswordResetTokenRespository;
import com.Bookstore.respository.UserPaymentRespository;
import com.Bookstore.respository.UserRespository;
import com.Bookstore.respository.UserShippingAddressRespository;
import com.Bookstore.security.PasswordResetToken;

@Service
public class UserServiceImpl implements UserService
{
	// private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserRespository					userRepository;

	@Autowired
	private BCryptPasswordEncoder			passwordEncoder;

	@Autowired
	private PasswordResetTokenRespository	passwordResetTokenRespository;
	
	@Autowired
	UserPaymentRespository userPaymentRespository;
	
	@Autowired
	UserShippingAddressRespository userShippingAddressRespository;

	@Override
	public User findByUsername(String username)
	{
		return userRepository.findByUsername(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		User user = userRepository.findByUsername(username);
		if (user == null)
		{
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles)
	{
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public PasswordResetToken getPasswordResetToken(String token)
	{
		return passwordResetTokenRespository.findByToken(token);
	}

	@Override
	public void createPasswordResetTokenForUser(User user, String token)
	{
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenRespository.save(myToken);
	}

	@Override
	public User findByEmail(String email)
	{
		return userRepository.findByEmail(email);
	}
	 @Transactional
	public User CreateNewUser(String userEmail, String username, String encryptedPassword)
	{
		User user = new User();
		user.setUsername(username);
		user.setEmail(userEmail);
		user.setPassword(encryptedPassword);
		user.setRoles(Arrays.asList(new Role("ROLE_ADMIN")));
		
		 ShoppingCart shoppingCart=new ShoppingCart();
		 shoppingCart.setUser(user);
		 user.setShoppingCart(shoppingCart);
		 
		 user.setUserShippingList(new ArrayList<UserShipping>());
		 user.setUserPaymentList(new ArrayList<UserPayment>());
		
		return userRepository.save(user);
	}

	@Override
	public User save(User user)
	{
		return userRepository.save(user);
		/*
		 * User user = new User();
		 * user.setFirstName(registration.getFirstName());
		 * user.setLastName(registration.getLastName());
		 * user.setEmail(registration.getEmail());
		 * user.setPassword(passwordEncoder.encode(registration.getPassword()));
		 * user.setRoles(Arrays.asList(new Role("ROLE_USER")));
		 * return userRepository.save(user);
		 */
	}

	@Override
	public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user)
	{
		userPayment.setUser(user);
		userPayment.setUserBilling(userBilling);
		userPayment.setDefaultPayment(true);
		userBilling.setUserPayment(userPayment);
		user.getUserPaymentList().add(userPayment);
		save(user);
		
	}

	@Override
	public void setUserDefaultPayment(Long userPaymentId, User user)
	{
		List<UserPayment> userPaymentList  = (List<UserPayment>) userPaymentRespository.findAll();
		
		for(UserPayment userPayment : userPaymentList )
		{
			if(userPayment.getId()==userPaymentId)
			{
				userPayment.setDefaultPayment(true);
				userPaymentRespository.save(userPayment);
			}else
			{
				userPayment.setDefaultPayment(false);
				userPaymentRespository.save(userPayment);
			}
		}
	}

	@Override
	public void UpdateUserShipping(UserShipping userShipping, User user)
	{
		userShipping.setUser(user);
		userShipping.setUserShippingDefault(true);
		user.getUserShippingList().add(userShipping);
		save(user);
	}

	@Override
	public void setUserDefaultShipping(Long userShippingId, User user)
	{
       List<UserShipping> userShippingList  = (List<UserShipping>) userShippingAddressRespository.findAll();
		
		for(UserShipping userShipping : userShippingList )
		{
			if(userShipping.getId()==userShippingId)
			{
				userShipping.setUserShippingDefault(true);;
				userShippingAddressRespository.save(userShipping);
			}else
			{
				userShipping.setUserShippingDefault(false);;
				userShippingAddressRespository.save(userShipping);
			}
		}	
	}

	@Override
	public User findById(Long id)
	{
		return userRepository.findById(id).get();
	}



}
