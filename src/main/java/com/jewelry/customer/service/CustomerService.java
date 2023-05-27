package com.jewelry.customer.service;

import com.jewelry.customer.domain.CustomerTO;
import com.jewelry.customer.domain.CustomerVO;

import java.util.Map;

public interface CustomerService {
	
	Map<String, Object> findAllCustomer(CustomerTO to);
	
	CustomerVO findCustomerByNo(CustomerTO to);
	
	String insertCustomer(CustomerTO to);
	
	String updateCustomer(CustomerTO to);

	String updateCustomerToDelete(CustomerTO to);

	String updateCustomersToDelete(CustomerTO to);

}
