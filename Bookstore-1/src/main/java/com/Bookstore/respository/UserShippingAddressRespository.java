package com.Bookstore.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Bookstore.model.UserShipping;

public interface UserShippingAddressRespository extends JpaRepository<UserShipping, Long>
{

}
