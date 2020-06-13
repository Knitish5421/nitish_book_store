package com.Bookstore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Bookstore.model.UserShipping;
import com.Bookstore.respository.UserShippingAddressRespository;

@Service
public class UserShippingAddressServiceImp implements UserShippingAddressService
{
   @Autowired
   UserShippingAddressRespository userShippingAddressRespository;

@Override
public UserShipping findById(Long id)
{
	return userShippingAddressRespository.findById(id).get();
}

@Override
public void removeById(Long id)
{
	userShippingAddressRespository.deleteById(id);	
}
}
