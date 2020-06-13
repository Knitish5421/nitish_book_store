package com.Bookstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.Bookstore.service.impl.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

	@Autowired
	private UserService				userSecurityService;

	private static final String[]	PUBLIC_MATCHERS	=
	{
		"/css/**",
		"/js/**",
		"/img/**",
		"/",
		"/newUser**",
		"/forgetPassword**",
		"/editYourProfile**",
		"/myAccount**",
		"/login",
		"/bookshelf",
		"/bookDetail",
		"/faq",
		"/hours",
		"/searchByCategory",
		"/searchBook"
	};

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		
		//from pjt
		/*
		 * http
		 * .authorizeRequests()
		 * .antMatchers(PUBLIC_MATCHERS
		 * ).permitAll()
		 * .anyRequest().authenticated()
		 * .and()
		 * .formLogin()
		 * .loginPage("/login")
		 * .permitAll()
		 * .and()
		 * .logout()
		 * .invalidateHttpSession(true)
		 * .clearAuthentication(true)
		 * .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		 * .logoutSuccessUrl("/login?logout")
		 * .permitAll();
		 */
		
		
		//from video
		
		  http
		  .authorizeRequests().antMatchers(PUBLIC_MATCHERS)
		  .permitAll().anyRequest().authenticated();
		  
		  http
		  .csrf().disable().cors().disable()
		  .formLogin().failureUrl("/login?error").defaultSuccessUrl("/")
		  .loginPage("/login")
		  .permitAll()
		  .and()
		  .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		  .logoutSuccessUrl("/?logout").deleteCookies("rember-me")
		  
		  .permitAll()
		  .and()
		  .rememberMe();
		
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userSecurityService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.authenticationProvider(authenticationProvider());
	}
}
