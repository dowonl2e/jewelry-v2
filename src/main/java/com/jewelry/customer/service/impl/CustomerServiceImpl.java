package com.jewelry.customer.service.impl;

import com.jewelry.annotation.MenuAuthAnt;
import com.jewelry.customer.domain.CustomerTO;
import com.jewelry.customer.domain.CustomerVO;
import com.jewelry.customer.mapper.CustomerMapper;
import com.jewelry.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

	private final CustomerMapper customerMapper;

	@Transactional(readOnly = true)
	@MenuAuthAnt
	@Override
	public Map<String, Object> findAllCustomer(CustomerTO to) {
		Map<String, Object> response = new HashMap<>();

		to.setTotalcount(customerMapper.selectCustomerListCount(to));
		response.put("list", customerMapper.selectCustomerList(to));
		response.put("params", to);
		
		return response;
	}

	@Transactional(readOnly = true)
	@MenuAuthAnt
	@Override
	public CustomerVO findCustomerByNo(Long customerno) {
		return customerMapper.selectCustomer(customerno);
	}

	@Transactional
	@Override
	public String insertCustomer(CustomerTO to) {
		int res = 0;
		try {
			res = customerMapper.insertCustomer(to);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return res > 0 ? "success" : "fail";
	}

	@Transactional
	@Override
	public String updateCustomer(CustomerTO to) {
		int res = 0;
		try {
			res = customerMapper.updateCustomer(to);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return res > 0 ? "success" : "fail";
	}

	@Transactional
	@Override
	public String updateCustomerToDelete(CustomerTO to) {
		int res = 0;
		try {
			res = customerMapper.updateCustomerToDelete(to);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return res > 0 ? "success" : "fail";
	}

	@Transactional
	@Override
	public String updateCustomersToDelete(CustomerTO to) {
		String result = "fail";
		try {
			if(to.getCustomer_no_arr() != null && to.getCustomer_no_arr().length > 0) {
				int res = customerMapper.updateCustomersToDelete(to);
				result = res > 0 ? "success" : "fail";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result = "fail";
		}

		return result;
	}
}
