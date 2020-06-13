package com.Bookstore.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.Bookstore.model.BillingAddress;
import com.Bookstore.model.CartItem;
import com.Bookstore.model.Order;
import com.Bookstore.model.Payment;
import com.Bookstore.model.ShippingAddress;
import com.Bookstore.model.ShoppingCart;
import com.Bookstore.model.User;
import com.Bookstore.model.UserBilling;
import com.Bookstore.model.UserPayment;
import com.Bookstore.model.UserShipping;
import com.Bookstore.service.impl.BillingAddressService;
import com.Bookstore.service.impl.CartItemService;
import com.Bookstore.service.impl.IndiaConstants;
import com.Bookstore.service.impl.OrderService;
import com.Bookstore.service.impl.PaymentService;
import com.Bookstore.service.impl.ShippingAddressService;
import com.Bookstore.service.impl.ShoppingCartService;
import com.Bookstore.service.impl.UserPaymentService;
import com.Bookstore.service.impl.UserService;
import com.Bookstore.service.impl.UserShippingAddressService;
import com.Bookstore.utility.MailConstructor;

@Controller
public class CheckOutController
{
  
	ShippingAddress shippingAddress =new ShippingAddress();
	BillingAddress billingAddress = new BillingAddress();
	Payment payment =new Payment();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ShippingAddressService shippingAddressService;
	
	@Autowired
	private BillingAddressService billingAddressService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private UserShippingAddressService userShippingAddressService;
	
	@Autowired
	private UserPaymentService userPaymentService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private MailConstructor mailConstructor;
	
	@RequestMapping("/checkout")
	public String checkout(@RequestParam("id") Long cartId,
			@RequestParam(value="missingRequiredField", required = false) boolean missingRequiredField,
			Model model, Principal principal) {
		User user= userService.findByEmail(principal.getName());
		
		if(cartId != user.getShoppingCart().getId())
		{
			return "badRequestPage";
		}
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
		
		if(cartItemList.size() ==0)
		{
			model.addAttribute("emptyCart", true);
			return "forward:/cart";
		}
		for(CartItem cartItem : cartItemList)
		{
			if(cartItem.getBook().getInStockNumber() < cartItem.getQty())
			{
				model.addAttribute("notEnoughStock", true);
				return "forward:/cart";
			}
		}
		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();
		

		if(userShippingList.size()!=0)
		{
			model.addAttribute("emptyShippingList", true);
		}
		else
		{
			model.addAttribute("emptyShippingList", false);
		}
		
		if(userPaymentList.size()!=0)
		{
			model.addAttribute("emptyPaymentList", true);
		}
		else
		{
			model.addAttribute("emptyPaymentList", false);
		}
		
		
		for(UserShipping userShipping : userShippingList)
		{
			if(userShipping.isUserShippingDefault())
			{
				shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			}
		}
		
		for(UserPayment userPayment : userPaymentList)
		{
			if(userPayment.isDefaultPayment())
			{
				paymentService.setByUserPayment(userPayment, payment);
				billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
			}
		}
		model.addAttribute("userPaymentList", userPaymentList);
		model.addAttribute("userShippingList", userShippingList);
		
		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("payment", payment);
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", user.getShoppingCart());
		List<String> stateList = IndiaConstants.listOfINDIAStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("showMyAccountTab", true);
		model.addAttribute("showLogoutTab", true);
		
		if(missingRequiredField)
		{
			model.addAttribute("missingRequiredField", true);
		}
		
		return "checkout";
		
	}
	
	@RequestMapping(value="/checkout", method=RequestMethod.POST)
	public String checkoutPost(@RequestParam("id") Long cartId,
			@ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
			@ModelAttribute("billingAddress") BillingAddress billingAddress,
			@ModelAttribute("payment") Payment payment,
			@ModelAttribute("billingSameAsShipping") String billingSameAsShipping,
			@ModelAttribute("shippingMethod") String shippingMethod,
			Model model, Principal principal) {
		
		ShoppingCart  shoppingCart = userService.findByEmail(principal.getName()).getShoppingCart();
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		model.addAttribute("cartItemList", cartItemList);
		
		if(billingSameAsShipping.equals("true")
				) {
			billingAddress.setBillingAddressName(shippingAddress.getShippingAddressName());
			billingAddress.setBillingAddressStreet1(shippingAddress.getShippingAddressStreet1());
			billingAddress.setBillingAddressStreet2(shippingAddress.getShippingAddressStreet2());
			billingAddress.setBillingAddressCity(shippingAddress.getShippingAddressCity());
			billingAddress.setBillingAddressState(shippingAddress.getShippingAddressState());
			billingAddress.setBillingAddressCountry(shippingAddress.getShippingAddressCountry());
			billingAddress.setBillingAddressZipcode(shippingAddress.getShippingAddressZipcode());
		}
		if(shippingAddress.getShippingAddressStreet1().isEmpty() ||shippingAddress.getShippingAddressStreet2().isEmpty()
			|| shippingAddress.getShippingAddressCity().isEmpty() || shippingAddress.getShippingAddressState().isEmpty()
			|| shippingAddress.getShippingAddressCountry().isEmpty() || shippingAddress.getShippingAddressName().isEmpty()
			|| shippingAddress.getShippingAddressZipcode().isEmpty() 
			|| payment.getCvc()==0 ||billingAddress.getBillingAddressStreet1().isEmpty() || billingAddress.getBillingAddressStreet2().isEmpty()
			||billingAddress.getBillingAddressCity().isEmpty() || billingAddress.getBillingAddressState().isEmpty()
			|| billingAddress.getBillingAddressCountry().isEmpty() || billingAddress.getBillingAddressName().isEmpty()
			|| billingAddress.getBillingAddressZipcode().isEmpty())
		{
			return "redirect:/checkout?id="+shoppingCart.getId() + "&missingRequiredField=true";
			
		}
		User user = userService.findByEmail(principal.getName());

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
	
		String estimatedDeliveryDate;
		Calendar c = Calendar.getInstance();
		
		if(shippingMethod.equals("groundShipping"))
		{
		   c.add(Calendar.DAY_OF_MONTH, 5);  
		   estimatedDeliveryDate =dateFormate.format(c.getTime());
		   
		}else {
			c.add(Calendar.DAY_OF_MONTH, 3); 
			 estimatedDeliveryDate =dateFormate.format(c.getTime());
		}
		
		
		Order order = orderService.createOrder(shoppingCart,shippingAddress,billingAddress,payment,shippingMethod, user, estimatedDeliveryDate);
		
		mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order , Locale.ENGLISH));
		
		shoppingCartService.clearShoppingCart(shoppingCart);
		model.addAttribute("showMyAccountTab", true);
		model.addAttribute("showLogoutTab", true);
	
		model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);
		
		return "orderSubmittedPage";
	}
	
	@RequestMapping("/setShippingAddress")
	public String setShippingAddress(@RequestParam("id") Long userShippingId,
			Model model, Principal principal) {
		User user= userService.findByEmail(principal.getName());
		UserShipping userShipping = userShippingAddressService.findById(userShippingId);
		
		if(userShipping.getUser().getId()!=user.getId())
		{
			return "badRequestPage";
		}
		else
		{
			shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
			BillingAddress billingAddress = new BillingAddress();
			
			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList = user.getUserPaymentList();
			
			 model.addAttribute("emptyShippingList", true);
		
			if(userPaymentList.size()!=0)
			{
				model.addAttribute("emptyPaymentList", true);
			}
			else
			{
				model.addAttribute("emptyPaymentList", false);
			}
			
			model.addAttribute("userPaymentList", userPaymentList);
			model.addAttribute("userShippingList", userShippingList);
			
			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());
			List<String> stateList = IndiaConstants.listOfINDIAStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			model.addAttribute("classActiveShipping", true);
			model.addAttribute("showMyAccountTab", true);
			model.addAttribute("showLogoutTab", true);
			return "checkout";
		}
			
	}
	
	@RequestMapping("/setPaymentMethod")
	public String setPayment(@RequestParam("userPaymentId") Long userPaymentId,
			Model model, Principal principal) {
		User user= userService.findByEmail(principal.getName());
		UserPayment userPayment = userPaymentService.findById(userPaymentId);
		UserBilling userBilling = userPayment.getUserBilling();
		
		if(userPayment.getUser().getId()!=user.getId())
		{
			return "badRequestPage";
		}
		else
		{
			paymentService.setByUserPayment(userPayment, payment);
			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
			billingAddressService.setByUserBilling(userBilling, billingAddress);
			
			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList = user.getUserPaymentList();
			
			if(userShippingList.size()!=0)
			{
				model.addAttribute("emptyShippingList", true);
			}
			else
			{
				model.addAttribute("emptyShippingList", false);
			}
		
			model.addAttribute("emptyPaymentList", true);
		
			
			model.addAttribute("userPaymentList", userPaymentList);
			model.addAttribute("userShippingList", userShippingList);
			
			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());
			List<String> stateList = IndiaConstants.listOfINDIAStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			model.addAttribute("classActivePayment", true);
			model.addAttribute("showMyAccountTab", true);
			model.addAttribute("showLogoutTab", true);
			return "checkout";
		}
			
	}
}
