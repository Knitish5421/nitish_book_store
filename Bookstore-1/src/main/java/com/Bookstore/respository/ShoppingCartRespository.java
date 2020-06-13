package com.Bookstore.respository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.Bookstore.model.ShoppingCart;

public interface ShoppingCartRespository extends CrudRepository<ShoppingCart, Long>
{
	@Modifying
	@Query("update ShoppingCart s set s.GrandTotal = :GrandTotal where s.id = :id")
	void updateShoppingCartGrandTotalById(
	  @Param("GrandTotal") BigDecimal GrandTotal,
	  @Param("id") Long id);
}
