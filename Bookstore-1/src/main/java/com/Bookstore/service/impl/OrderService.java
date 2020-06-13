package com.Bookstore.service.impl;


import com.Bookstore.model.BillingAddress;
import com.Bookstore.model.Order;
import com.Bookstore.model.Payment;
import com.Bookstore.model.ShippingAddress;
import com.Bookstore.model.ShoppingCart;
import com.Bookstore.model.User;

public interface OrderService
{
	Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress, BillingAddress billingAddress,
			          Payment payment, String shippingMethod, User user, String estimatedDeliveryDate);
	
	Order findById(Long id);

}
