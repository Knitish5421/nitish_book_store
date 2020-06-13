package com.Bookstore.service.impl;

import com.Bookstore.model.BillingAddress;
import com.Bookstore.model.UserBilling;

public interface BillingAddressService
{
	public BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);
   
}
