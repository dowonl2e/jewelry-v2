package com.jewelry.order.service;

import com.jewelry.order.domain.OrderTO;
import com.jewelry.order.domain.OrderVO;
import com.jewelry.stock.domain.StockTO;

import java.util.Map;

public interface OrderService {
	
	Map<String, Object> findAllOrder(OrderTO to);
	
	String insertOrder(OrderTO to);
	
	OrderVO findOrderByNo(OrderTO to);
	
	String updateOrder(OrderTO to);
	
	String updateOrdersStep(OrderTO to);
	
	String updateOrdersToDelete(OrderTO to);

	String updateOrdersCustomer(OrderTO to);

	String updateOrdersVender(OrderTO to);

	String updateOrderToDelete(OrderTO to);
	
	Map<String, Object> findAllOrdersNo(OrderTO to);
	
	Map<String, Object> findAllCustomerOrder(OrderTO to);

	String insertOrdersToStock(StockTO to, OrderTO orderto);
}
