package com.Bookstore.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Bookstore.model.Order;

public interface OrderRespository extends JpaRepository<Order, Long>
{

}
