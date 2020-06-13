package com.Bookstore.respository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Bookstore.model.CartItem;
import com.Bookstore.model.Order;
import com.Bookstore.model.ShoppingCart;

@Transactional
public interface CartItemRespository extends JpaRepository<CartItem, Long>
{
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	List<CartItem>  findByOrder(Order order);
	
	@Modifying
	@Query("update CartItem c set c.qty = :qty, c.subTotal = :subTotal where c.id = :id")
	void updateCartItemQtyById(@Param("qty") int qty,
	  @Param("subTotal") BigDecimal subTotal,
	  @Param("id") Long id);
}
