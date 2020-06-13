package com.Bookstore.service.impl;

import com.Bookstore.model.UserPayment;

public interface UserPaymentService
{
	UserPayment findById(Long id);
	void removeById(Long id);
}
