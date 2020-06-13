package com.Bookstore.service.impl;

import com.Bookstore.model.Payment;
import com.Bookstore.model.UserPayment;

public interface PaymentService
{
 
	public Payment setByUserPayment(UserPayment userPayment, Payment payment);
}
