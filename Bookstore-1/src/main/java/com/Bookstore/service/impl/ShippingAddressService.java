package com.Bookstore.service.impl;

import com.Bookstore.model.ShippingAddress;
import com.Bookstore.model.UserShipping;

public interface ShippingAddressService
{
	public ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);

}
