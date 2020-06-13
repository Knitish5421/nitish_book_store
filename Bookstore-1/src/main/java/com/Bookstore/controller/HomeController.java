package com.Bookstore.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.Bookstore.model.Book;
import com.Bookstore.model.CartItem;
import com.Bookstore.model.Order;
import com.Bookstore.model.User;
import com.Bookstore.model.UserBilling;
import com.Bookstore.model.UserPayment;
import com.Bookstore.model.UserShipping;
import com.Bookstore.security.PasswordResetToken;
import com.Bookstore.service.impl.BookService;
import com.Bookstore.service.impl.CartItemService;
import com.Bookstore.service.impl.IndiaConstants;
import com.Bookstore.service.impl.OrderService;
import com.Bookstore.service.impl.UserPaymentService;
import com.Bookstore.service.impl.UserService;
import com.Bookstore.service.impl.UserServiceImpl;
import com.Bookstore.service.impl.UserShippingAddressService;
import com.Bookstore.utility.MailConstructor;
import com.Bookstore.utility.PasswordBCryEncoder;

@Controller
public class HomeController
{

	@Autowired
	UserService						userService;

	@Autowired
	UserServiceImpl					userServiceImpl;

	@Autowired
	private JavaMailSender			mailSender;

	@Autowired
	private MailConstructor			mailConstructor;

	@Autowired
	private BCryptPasswordEncoder	passwordEncoder;
	
	@Autowired
	BookService                      bookService;
	
	@Autowired
	UserPaymentService             userPaymentService;
	
	@Autowired
	UserShippingAddressService     userShippingAddressService;
	
	@Autowired
	OrderService                    orderService;
	
	@Autowired
	CartItemService                cartItemService;

	@GetMapping("/")
	public String index(Model model)
	{
		model.addAttribute("showMyAccountLoginTab", true);
		model.addAttribute("classActiveLogin", true);
		return "index";
	}

	@GetMapping("/login")
	public String login(Model model)
	{
		model.addAttribute("showMyAccountTab", true);
		model.addAttribute("showLogoutTab", true);
		model.addAttribute("classActiveLogin", true);
		return "myAccount";
	}
	
	@RequestMapping("/hours")
	public String hours()
	{
		return "hours";
	}
	
	@RequestMapping("/faq")
	public String faq()
	{
		return "faq";
	}
	
	@RequestMapping(value="/bookshelf")
	public String bookShelf(Model model,Principal principal)
	{
		if(principal!=null)
		{
			String email= principal.getName();
			User user = userService.findByEmail(email);
			model.addAttribute("user", user);
		}
		model.addAttribute("showMyAccountTab", true);
		model.addAttribute("showLogoutTab", true);
		List<Book> bookList=bookService.findAll();
		model.addAttribute("bookList", bookList);
		model.addAttribute("activeAll", true);
		return "bookShelf";
	}
	
	@RequestMapping(value="/bookDetail")
	public String bookDetail(@PathParam("id") Long id, Model model, Principal principal)
	{
		if(principal!=null)
		{
			String email= principal.getName();
			User user = userService.findByEmail(email);
			model.addAttribute("user", user);
		}
		Book book = bookService.findById(id);
		model.addAttribute("book", book);
		List<Integer> qtyList=Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);
		return "bookDetails";
	}

	@RequestMapping(value ="/forgetPassword" , method = {RequestMethod.GET,RequestMethod.POST})
	public String forgetPassword(Model model,HttpServletRequest request, Locale locale, @ModelAttribute("email") String email)
	{
		model.addAttribute("classActiveForgetPassword", true);
		User user = userService.findByEmail(email);
		if (user == null)
		{
			model.addAttribute("emailNotExist", true);
			return "myAccount";
		}
		
		String password = PasswordBCryEncoder.randomPassword();
		String encryptedPassword = passwordEncoder.encode(password);
		user.setPassword(encryptedPassword);
		userService.save(user);
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage newEmail = mailConstructor.constructorResetTokenEmail(appUrl, request.getLocale(), token, user, password);
		mailSender.send(newEmail);
		model.addAttribute("forgetPasswordEmailSent", true);
		return "myAccount";
	}

	@RequestMapping(value = "/newUser", method ={
		RequestMethod.GET,
		RequestMethod.POST })
	public String newUserPost(HttpServletRequest request, Locale locale, @ModelAttribute("email") String userEmail, @ModelAttribute("username") String username,
			Model model)
	{
		
		model.addAttribute("showMyAccountTab", true);
		model.addAttribute("showLogoutTab", true);
		
		
		model.addAttribute("classActiveNewAccount", true);
		if (userService.findByUsername(username) != null)
		{
			model.addAttribute("usernameExists", true);
			return "myAccount";
		}

		if (userService.findByEmail(userEmail) != null)
		{
			model.addAttribute("emailExists", true);
			return "myAccount";
		}
		String password = PasswordBCryEncoder.randomPassword();
		String encryptedPassword = passwordEncoder.encode(password);

		User user = userServiceImpl.CreateNewUser(userEmail, username, encryptedPassword);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage email = mailConstructor.constructorResetTokenEmail(appUrl, request.getLocale(), token, user, password);
		mailSender.send(email);
		model.addAttribute("emailSent", "true");
		return "myAccount";
	}

	@RequestMapping(value="/editYourProfile")
	public String newUser(Locale locale, @RequestParam("token") String token, Model model)
	{
		PasswordResetToken PasswordToken = userService.getPasswordResetToken(token);
		if (PasswordToken == null)
		{
			String message = "Invaild Token.";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}
		User user = PasswordToken.getUser();
		String username = user.getUsername();
		UserDetails userDetails = userService.loadUserByUsername(username);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		model.addAttribute("user", user);
		model.addAttribute("classActiveEdit", true);
		return "myProfile";
	}
	
	@RequestMapping(value="/myProfile")
	public String myProfile(Principal principal,  Model model)
	{
		String email=principal.getName();
		User user = userService.findByEmail(email);
		model.addAttribute("user", user);
		model.addAttribute("showLogoutTab", true);
		if(user.getUserPaymentList()!=null)
		{
		  model.addAttribute("userPaymentList", user.getUserPaymentList());
		  model.addAttribute("userShippingList", user.getUserShippingList());
		  model.addAttribute("orderList", user.getOrderList());
		  
		  UserShipping userShipping = new UserShipping();
		  model.addAttribute("userShipping", userShipping);
		}
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		List<String> stateList = IndiaConstants.listOfINDIAStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("classActiveEdit", true);
		
		
		return "myProfile";
	}
	
	@RequestMapping(value="/listOfCreditCards")
	public String listOfCreditCards(Principal principal,  Model model , HttpServletRequest request)
	{
		String email=principal.getName();
		User user = userService.findByEmail(email);
		model.addAttribute("user", user);
		if(user.getUserPaymentList()!=null)
		{
		  model.addAttribute("userPaymentList", user.getUserPaymentList());
		  model.addAttribute("userShippingList", user.getUserShippingList());
		  model.addAttribute("orderList", user.getOrderList());
		}
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("showMyAccountTab", true);
		model.addAttribute("showLogoutTab", true);
		return "myProfile";
	}
	
	
	@RequestMapping(value="/addNewCreditCard")
	public String addNewCreditCard(Model model, Principal principal)
	{
		String email=principal.getName();
		User user = userService.findByEmail(email);
		model.addAttribute("user", user);
		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("listOfShippingAddress", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("showMyAccountTab", true);
		model.addAttribute("showLogoutTab", true);
		
		UserBilling userBilling=new UserBilling();
		UserPayment userPayment = new UserPayment();
		
		model.addAttribute("userBilling", userBilling);
		model.addAttribute("userPayment", userPayment);
		
		List<String> stateList = IndiaConstants.listOfINDIAStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
	    model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
	}
	
	
	@RequestMapping(value="/addNewCreditCard", method = RequestMethod.POST)
	public String addNewCreditCardPost(Model model, Principal principal, @ModelAttribute("userPayment")
	 UserPayment userPayment, @ModelAttribute("userBilling") UserBilling userBilling)
	{
		String email=principal.getName();
		User user = userService.findByEmail(email);
		userService.updateUserBilling(userBilling, userPayment, user);
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddress", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("showMyAccountTab", true);
		model.addAttribute("showLogoutTab", true);
		return "myProfile";
	}
	
	@RequestMapping(value="/updateCreditCard")
	public String updateCreditCard(Model model, Principal principal, @ModelAttribute("id")
	Long creditCardId)
	{
		String email=principal.getName();
		User user = userService.findByEmail(email);
		UserPayment userPayment = userPaymentService.findById(creditCardId);
		if(user.getId() !=userPayment.getUser().getId()) {
			return "badRequestPage";
		}
		else
		{
			model.addAttribute("user", user);
			UserBilling userBilling = userPayment.getUserBilling();
			model.addAttribute("userPayment", userPayment);
			model.addAttribute("userBilling", userBilling);
			
			List<String> stateList = IndiaConstants.listOfINDIAStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());
			
			model.addAttribute("addNewCreditCard", true);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("showMyAccountTab", true);
			model.addAttribute("showLogoutTab", true);
			
		}
		return "myProfile";
		
	}
	
	@RequestMapping(value="/setDefaultPayment")
	public String setDefaultPayment(Model model, Principal principal, @ModelAttribute("defaultUserPaymentId")
	Long defaultPaymentId)
	{
		String email=principal.getName();
		User user = userService.findByEmail(email);
		userService.setUserDefaultPayment(defaultPaymentId, user);
		model.addAttribute("user", user);
		model.addAttribute("listOfShippingAddress", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfCreditCards", true);	
		model.addAttribute("showMyAccountTab", true);
		model.addAttribute("showLogoutTab", true);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		return "myProfile";
	}
	
	@RequestMapping(value="/removeCreditCard")
	public String removeCreditCard(Model model, Principal principal, @ModelAttribute("id") Long creditCardId)
	{
		String email=principal.getName();
		User user = userService.findByEmail(email);
		UserPayment userPayment = userPaymentService.findById(creditCardId);
		if(user.getId() !=userPayment.getUser().getId()) {
			return "badRequestPage";
		}
		else
		{
			model.addAttribute("user", user);
			userPaymentService.removeById(creditCardId);
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());
			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("showMyAccountTab", true);
			model.addAttribute("showLogoutTab", true);			
		}
		return "myProfile";
		
	}
	
	/* shipping Address controller */
	
	@RequestMapping(value="/listOfShippingAddresses")
	public String listOfShippingAddress(Principal principal,  Model model , HttpServletRequest request)
	{
		String email=principal.getName();
		User user = userService.findByEmail(email);
		model.addAttribute("user", user);
		if(user.getUserPaymentList()!=null)
		{
		  model.addAttribute("userPaymentList", user.getUserPaymentList());
		  model.addAttribute("userShippingList", user.getUserShippingList());
		  model.addAttribute("orderList", user.getOrderList());
		}
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("showMyAccountTab", true);
		model.addAttribute("showLogoutTab", true);
		return "myProfile";
	}

	
	@RequestMapping(value="/addNewShippingAddress")
	public String addNewShippingAddress(Model model, Principal principal)
	{
		String email=principal.getName();
		User user = userService.findByEmail(email);
		model.addAttribute("user", user);
		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("showMyAccountTab", true);
		model.addAttribute("showLogoutTab", true);
		
	    UserShipping userShipping=new UserShipping();
		model.addAttribute("userShipping", userShipping);
		
		List<String> stateList = IndiaConstants.listOfINDIAStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
	    model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
	}
	
	@RequestMapping(value="/addNewShippingAddress", method = RequestMethod.POST)
	public String addNewShippingAddressPost(Model model, Principal principal,@ModelAttribute("userShipping") UserShipping userShipping)
	{
		String email=principal.getName();
		User user = userService.findByEmail(email);
		userService.UpdateUserShipping(userShipping, user);
		model.addAttribute("user", user);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);
	    model.addAttribute("listOfShippingAddresses", true);
	    model.addAttribute("showMyAccountTab", true);
		model.addAttribute("showLogoutTab", true);

		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
	    model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
	}
	
	
	@RequestMapping(value="/updateUserShipping")
	public String updateUserShipping(Model model, Principal principal, @ModelAttribute("id")
	Long shippingAddressId)
	{
		String email=principal.getName();
		User user = userService.findByEmail(email);
		  UserShipping userShipping =userShippingAddressService.findById(shippingAddressId);
		  if(user.getId() !=userShipping.getUser().getId()) {
		  return "badRequestPage";
		  }
		else
		{
			model.addAttribute("user", user);
			model.addAttribute("userShipping", userShipping);
			
			List<String> stateList = IndiaConstants.listOfINDIAStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			model.addAttribute("showMyAccountTab", true);
			model.addAttribute("showLogoutTab", true);
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());
			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveShipping", true);
			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("addNewShippingAddress", true);
			
		}
		return "myProfile";
		
	}
	
	@RequestMapping(value="/setDefaultShippingAddress")
	public String setDefaultShippingAddress(Model model, Principal principal, @ModelAttribute("defaultShippingAddressId")
	Long defaultShippingId)
	{
		String email=principal.getName();
		User user = userService.findByEmail(email);
		userService.setUserDefaultShipping(defaultShippingId,user);
		
		model.addAttribute("user", user);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);	
		model.addAttribute("showMyAccountTab", true);
		model.addAttribute("showLogoutTab", true);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		return "myProfile";
	}
	
	  @RequestMapping(value="/removeUserShipping")
	  public String removeShippingAddress(Model model, Principal principal, @ModelAttribute("id") Long shippingAddressId)
	  {
	  model.addAttribute("showMyAccountTab", true);
	  model.addAttribute("showLogoutTab", true);
	  String email=principal.getName();
	  User user = userService.findByEmail(email);
	  UserShipping userShipping =userShippingAddressService.findById(shippingAddressId);
	  if(user.getId() !=userShipping.getUser().getId()) {
	  return "badRequestPage";
	  }
	  else
	  {
	  model.addAttribute("user", user);
	  userShippingAddressService.removeById(shippingAddressId);
	  model.addAttribute("userPaymentList", user.getUserPaymentList());
	  model.addAttribute("userShippingList", user.getUserShippingList());
	  model.addAttribute("orderList", user.getOrderList());
	  model.addAttribute("listOfCreditCards", true);
	  model.addAttribute("classActiveShipping", true);
	  model.addAttribute("listOfShippingAddresses", true);
	  
	  }
	  return "myProfile";
	  
	  }
	  @RequestMapping(value="/updateUserInfo", method = RequestMethod.POST)
	  public String updateUserInfo(@ModelAttribute("user") User user,
			                       @ModelAttribute("newPassword") String newPassword,
			                       Model model)throws Exception {
		  User currentUser = userService.findById(user.getId());
		  if(currentUser == null)
		  {
			  throw new Exception("user not found");
		  }
		  
		  if(userService.findByEmail(user.getEmail())!=null)
		  { 
			  if(userService.findByEmail(user.getEmail()).getId()!=currentUser.getId())
			  {
			  model.addAttribute("emailExists", true);
			  return "myProfile";
			  }
		  }
		  
		  if(userService.findByUsername(user.getUsername())!=null)
		  {
			  if(userService.findByUsername(user.getUsername()).getId()!=currentUser.getId())
			  {
				  model.addAttribute("userExists", true);
			  }
		  }
		  
		  if(newPassword != null && !newPassword.isEmpty() && !newPassword.equals(""))
		  {
				
			  
			
			  currentUser.setPassword(passwordEncoder.encode(newPassword));
			
			  currentUser.setFirstName(user.getFirstName());
			  currentUser.setLastName(user.getLastName());
			  currentUser.setUsername(user.getUsername());
			  currentUser.setEmail(user.getEmail());
			  model.addAttribute("user", currentUser);
			  model.addAttribute("listOfShippingAddresses", true);
			  model.addAttribute("classActiveEdit", true);
			  model.addAttribute("updateSucess", true); 
			  model.addAttribute("listOfCreditCards", true);
			  userService.save(currentUser);
			  model.addAttribute("orderList", user.getOrderList());
			  
			  UserDetails userDetails = userService.loadUserByUsername(currentUser.getUsername());
			  Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
			  SecurityContextHolder.getContext().setAuthentication(authentication);
		  }
		return "myProfile";
	  }
	 
	  
	  @RequestMapping(value="/orderDetail")
		public String orderDetail(Model model, Principal principal, @ModelAttribute("id")
		Long orderId)
		{
			String email=principal.getName();
			User user = userService.findByEmail(email);
			Order order =orderService.findById(orderId);
			if(order.getUser().getId()!=user.getId()) {
				return "badRequestPage";
			}
			else
			{
				List<CartItem> cartItemList = cartItemService.findByOrder(order);
				model.addAttribute("cartItemList", cartItemList);
				model.addAttribute("user", user);
				model.addAttribute("order", order);
				model.addAttribute("userPaymentList", user.getUserPaymentList());
				model.addAttribute("userShippingList", user.getUserShippingList());
				model.addAttribute("orderList", user.getOrderList());
				
				UserShipping userShipping = new UserShipping();
				model.addAttribute("userShipping", userShipping);
				
				List<String> stateList = IndiaConstants.listOfINDIAStatesCode;
				Collections.sort(stateList);
				model.addAttribute("stateList", stateList);
		
				
				model.addAttribute("listOfShippingAddresses", true);
				model.addAttribute("classActiveOrder", true);
				model.addAttribute("listOfCreditCards", true);
				model.addAttribute("displayOrderDetails", true);
				model.addAttribute("showMyAccountTab", true);
				model.addAttribute("showLogoutTab", true);
				
			}
			return "myProfile";
			
		}
}
