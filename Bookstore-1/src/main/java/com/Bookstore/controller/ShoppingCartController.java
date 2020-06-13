package com.Bookstore.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.Bookstore.model.Book;
import com.Bookstore.model.CartItem;
import com.Bookstore.model.ShoppingCart;
import com.Bookstore.model.User;
import com.Bookstore.service.impl.BookService;
import com.Bookstore.service.impl.CartItemService;
import com.Bookstore.service.impl.ShoppingCartService;
import com.Bookstore.service.impl.UserService;

@Controller
public class ShoppingCartController
{
   @Autowired
   private UserService userService;
   
   @Autowired
   private CartItemService cartItemService;
   
   @Autowired
   private ShoppingCartService shoppingCartService;
   
   @Autowired
   private BookService bookService;
{
	
}
   
   
   @RequestMapping("/cart")
   public String shoppingCart(Model model, Principal principal)
   {
	   User user =userService.findByEmail(principal.getName());
	   
	   ShoppingCart shoppingCart = user.getShoppingCart();
	   
	   List<CartItem> cartItemList =cartItemService.findByShoppingCart(shoppingCart);
	   if(!shoppingCart.getCartItemList().isEmpty())
	   { 
	      shoppingCartService.updateShoppingCart(shoppingCart);
	   }
	   model.addAttribute("cartItemList", cartItemList);
	   model.addAttribute("showMyAccountTab", true);
	   model.addAttribute("showLogoutTab", true);
	   model.addAttribute("shoppingCart", shoppingCart);
	   return "ShoppingCart";
	   
   }
   
   @RequestMapping("/addItem")
   public String addItem(@ModelAttribute("book") Book book, @ModelAttribute("qty") String qty,Model model,
		   Principal principal)
   {
	   User user = userService.findByEmail(principal.getName());
	   book = bookService.findById(book.getId());
	   
	   if(Integer.parseInt(qty) > book.getInStockNumber())
	   {
		   model.addAttribute("notEnoughStock", true);
		   return "forward:/bookDetail?id="+book.getId();
	   }
	   CartItem cartItem =cartItemService.addBookToCartItem(book, user, Integer.parseInt(qty));
	   model.addAttribute("addBookSuccess", true);
	   return "forward:/bookDetail?id="+book.getId();
	
   }
   
   @RequestMapping("/removeItem")
   public String removCartItem(Model model, Principal principal,@RequestParam("id") Long cartId)
   {
	cartItemService.deleteById(cartId);
	return "forward:/cart";
   
   }
   
   @RequestMapping(value="/updateCartItem",method = RequestMethod.POST)
   public String updateCartItem( @RequestParam("id") Long cartId, @RequestParam("qty") int qty,@RequestParam("grandTotal") BigDecimal grandTotal )
   {
	  
		
		  CartItem cartItem = cartItemService.findById(cartId);
		  Long bookId = cartItem.getBook().getId();
		  Book book=bookService.findById(bookId);
		  Long shoppingCartId = cartItem.getShoppingCart().getId();
		  BigDecimal finalTotal = new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(qty));
		  // BigDecimal finalTotal= grandTotal.multiply(new BigDecimal(qty));
		  cartItemService.updateCartItemQtyById(qty,finalTotal, cartId);
		 // shoppingCartService.updateShoppingCartGrandTotalById(finalTotal,shoppingCartId);
		 
	return "redirect:/cart";
   
   }
}
