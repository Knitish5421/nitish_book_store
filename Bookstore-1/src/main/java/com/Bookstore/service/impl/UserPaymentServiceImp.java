package com.Bookstore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Bookstore.model.UserPayment;
import com.Bookstore.respository.UserPaymentRespository;

@Service
public class UserPaymentServiceImp implements UserPaymentService
{
	@Autowired
	UserPaymentRespository userPaymentRespository;

	@Override
	public UserPayment findById(Long id)
	{
		return userPaymentRespository.findById(id).get();
	}

	@Override
	public void removeById(Long id)
	{
		userPaymentRespository.deleteById(id);
	}

}
