package com.Bookstore.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Bookstore.model.Book;
import com.Bookstore.model.BookToCartItem;
import com.Bookstore.model.CartItem;
import com.Bookstore.model.Order;
import com.Bookstore.model.ShoppingCart;
import com.Bookstore.model.User;
import com.Bookstore.respository.BookToCartItemRespository;
import com.Bookstore.respository.CartItemRespository;

@Service
public class CartItemServiceImp implements CartItemService
{
	@Autowired 
	CartItemRespository cartItemRespository;
	
	@Autowired
	BookToCartItemRespository bookToCartItemRespository;

	@Override
	public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart)
	{
		return cartItemRespository.findByShoppingCart(shoppingCart);
	}

	@Override
	public CartItem updateCartItem(CartItem cartItem)
	{
        BigDecimal bigDecimal= new BigDecimal(cartItem.getBook().getOurPrice()).multiply(new BigDecimal(cartItem.getQty()));
        bigDecimal = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP);
        cartItem.setSubTotal(bigDecimal);
        cartItemRespository.save(cartItem);
		return cartItem;		
	}

	@Override
	public CartItem addBookToCartItem(Book book, User user, int qty)
	{
		List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());
		
		for(CartItem cartItem : cartItemList)
		{
			if(book.getId() == cartItem.getBook().getId())
			{
				cartItem.setQty(cartItem.getQty() +qty);
				cartItem.setSubTotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(qty)));
				cartItemRespository.save(cartItem);
				return cartItem;
			}
		}
		CartItem cartItem = new CartItem();
		cartItem.setShoppingCart(user.getShoppingCart());
		cartItem.setBook(book);
		
		cartItem.setQty(qty);
		cartItem.setSubTotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(qty)));
		cartItem = cartItemRespository.save(cartItem);
		
		BookToCartItem bookToCartItem = new BookToCartItem();
		bookToCartItem.setBook(book);
		bookToCartItem.setCartItem(cartItem);
		bookToCartItemRespository.save(bookToCartItem);
		return cartItem;
	}

	@Override
	public CartItem save(CartItem cartItem)
	{
		return cartItemRespository.save(cartItem);
	}

	@Override
	public List<CartItem> findByOrder(Order order)
	{
		return cartItemRespository.findByOrder(order);
	}

	@Override
	public void deleteById(Long id)
	{
		cartItemRespository.deleteById(id);		
	}

	@Override
	public void updateCartItemQtyById(int qty,BigDecimal subTotal, Long id)
	{
		cartItemRespository.updateCartItemQtyById(qty, subTotal, id);
	}
	
	@Override
	public CartItem findById(Long id)
	{
		return cartItemRespository.findById(id).get();
	}

}
