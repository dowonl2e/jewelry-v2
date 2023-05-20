package com.jewelry.customer.mapper;

import com.jewelry.customer.domain.CustomerTO;
import com.jewelry.customer.domain.CustomerVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomerMapper {
	
	int selectCustomerListCount(CustomerTO to);
	
	List<CustomerVO> selectCustomerList(CustomerTO to);
	
	CustomerVO selectCustomer(Long customerno);
	
	int insertCustomer(CustomerTO to) throws Exception;

	int updateCustomer(CustomerTO to) throws Exception;

	int updateCustomerToDelete(CustomerTO to) throws Exception;

	int updateCustomersToDelete(CustomerTO to) throws Exception;

}
