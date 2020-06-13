package com.Bookstore.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.Bookstore.model.UserPayment;

public interface UserPaymentRespository extends CrudRepository<UserPayment, Long>
{
}
