package com.Bookstore.service.impl;

import org.springframework.stereotype.Service;

import com.Bookstore.model.BillingAddress;
import com.Bookstore.model.UserBilling;

@Service
public class BillingAddressServiceImp implements BillingAddressService
{

	@Override
	public BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress)
	{
		billingAddress.setBillingAddressName(userBilling.getUserBillingName());
		billingAddress.setBillingAddressStreet1(userBilling.getUserBillingStreet1());
		billingAddress.setBillingAddressStreet2(userBilling.getUserBillingStreet2());
		billingAddress.setBillingAddressCity(userBilling.getUserBillingCity());
		billingAddress.setBillingAddressState(userBilling.getUserBillingState());
		billingAddress.setBillingAddressCountry(userBilling.getUserBillingCountry());
		billingAddress.setBillingAddressZipcode(userBilling.getUserBillingZipcode());
		
		return billingAddress;
	}

}
