package com.Bookstore.service.impl;

import com.Bookstore.model.UserShipping;

public interface UserShippingAddressService
{
	UserShipping findById(Long id);
	void removeById(Long id);

}
