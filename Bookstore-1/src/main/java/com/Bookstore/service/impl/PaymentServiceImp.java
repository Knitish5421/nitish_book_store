package com.Bookstore.service.impl;

import org.springframework.stereotype.Service;

import com.Bookstore.model.Payment;
import com.Bookstore.model.UserPayment;

@Service
public class PaymentServiceImp implements PaymentService
{

	@Override
	public Payment setByUserPayment(UserPayment userPayment, Payment payment)
	{
		payment.setType(userPayment.getType());
		payment.setHolderName(userPayment.getHolderName());
		payment.setCardName(userPayment.getCardName());
		payment.setCardNumber(userPayment.getCardNumber());
		payment.setExpiryMonth(userPayment.getExpiryMonth());
		payment.setExpiryYear(userPayment.getExpiryYear());
		payment.setCvc(userPayment.getCvc());
		return payment;
	}

}
