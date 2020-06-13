package com.Bookstore;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.Bookstore.model.Role;
import com.Bookstore.model.User;
import com.Bookstore.respository.UserRespository;
import com.Bookstore.utility.PasswordBCryEncoder;

@SpringBootApplication
public class Bookstore1Application 
{

	@Autowired
	UserRespository userRespository;

	public static void main(String[] args)
	{
		SpringApplication.run(Bookstore1Application.class, args);
	}

	/*
	 * @Override
	 * public void run(String... args) throws Exception
	 * {
	 * 
	 * 
	 * User user = new User();
	 * user.setUsername("NK");
	 * user.setEmail("knitish5421@gmail.com");
	 * user.setPassword("p");
	 * user.setRoles(Arrays.asList(new Role("ROLE_USER")));
	 * userRespository.save(user);
	 * 
	 * 
	 * }
	 */

}
