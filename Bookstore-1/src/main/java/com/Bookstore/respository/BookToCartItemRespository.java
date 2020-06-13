package com.Bookstore.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Bookstore.model.BookToCartItem;

public interface BookToCartItemRespository extends JpaRepository<BookToCartItem, Long>
{

}
