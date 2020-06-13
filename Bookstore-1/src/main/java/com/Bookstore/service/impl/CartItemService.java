package com.Bookstore.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.Bookstore.model.Book;
import com.Bookstore.model.CartItem;
import com.Bookstore.model.Order;
import com.Bookstore.model.ShoppingCart;
import com.Bookstore.model.User;

public interface CartItemService
{
  List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
  
  CartItem updateCartItem(CartItem cartItem);
  
  CartItem addBookToCartItem(Book book,User user, int qty);
  
  CartItem save(CartItem cartItem);
  
  List<CartItem> findByOrder(Order order);
  
  void deleteById(Long id);
  
  void updateCartItemQtyById(int qty, BigDecimal subTotal, Long id);
  
  CartItem findById(Long id);
   
  
}
