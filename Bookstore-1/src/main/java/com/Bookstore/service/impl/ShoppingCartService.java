package com.Bookstore.service.impl;

import java.math.BigDecimal;

import com.Bookstore.model.ShoppingCart;

public interface ShoppingCartService
{
	ShoppingCart updateShoppingCart(ShoppingCart shoppingCart);
	
	public void clearShoppingCart(ShoppingCart shoppingCart);
	
	void updateShoppingCartGrandTotalById( BigDecimal grandTotal, Long id);
}
