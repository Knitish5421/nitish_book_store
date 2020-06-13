package com.Bookstore.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Bookstore.model.BillingAddress;
import com.Bookstore.model.Book;
import com.Bookstore.model.CartItem;
import com.Bookstore.model.Order;
import com.Bookstore.model.Payment;
import com.Bookstore.model.ShippingAddress;
import com.Bookstore.model.ShoppingCart;
import com.Bookstore.model.User;
import com.Bookstore.respository.OrderRespository;

@Service
public class OrderServiceImp implements OrderService
{
	@Autowired
	private OrderRespository orderRespository;
	
	@Autowired
	private CartItemService cartItemService;

	@Override
	public synchronized Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress, BillingAddress billingAddress, Payment payment, String shippingMethod,
			User user, String estimatedDeliveryDate)

	{
		Order order = new Order();
		order.setOrderStatus("created");
		order.setShippingMethod(shippingMethod);
		order.setOrderDate(Calendar.getInstance().getTime());
		order.setOrderTotal(shoppingCart.getGrandTotal());
		order.setShippingDate(estimatedDeliveryDate);
		order = orderRespository.save(order);
		order.setPayment(payment);
		order.setBillingAddress(billingAddress);
		order.setShippingAddress(shippingAddress);
		
		
		
		  List<CartItem> cartItemList =
		  cartItemService.findByShoppingCart(shoppingCart);
		  
		  for(CartItem cartItem : cartItemList) { Book book = cartItem.getBook();
		  cartItem.setOrder(order);
		  book.setInStockNumber(book.getInStockNumber()-cartItem.getQty());
		  
		  } order.setCartItemList(cartItemList);
		 
		
		shippingAddress.setOrder(order);
		billingAddress.setOrder(order);
        payment.setOrder(order);
        order.setUser(user);
        order = orderRespository.save(order);		
		return order;
	}

	@Override
	public Order findById(Long id)
	{
		return orderRespository.findById(id).get();
	}

}
