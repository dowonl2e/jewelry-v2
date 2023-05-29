package com.jewelry.stock.service;

import com.jewelry.stock.domain.StockTO;
import com.jewelry.stock.domain.StockVO;

import java.util.List;
import java.util.Map;

public interface StockService {
	
	Map<String, Object> findAllStock(StockTO to);
	
	List<StockVO> findAllPrevStock();
	
	StockVO findStockByNo(StockTO to);
	
	StockVO findStockCustomerByNo(StockTO to);
	
	String insertStock(StockTO to);
	
	String updateStock(StockTO to);
	
	String updateStockToDelete(StockTO to);
	
	String updateStocksToDelete(StockTO to);
	
	String updateStocksToSale(StockTO to);
	
	String updateStocksRegDate(StockTO to);
	
	String updateStocksType(StockTO to);
	
	String updateStocksVender(StockTO to);
	
	Map<String, Object> findAllAccumulationStock(StockTO to);

	String insertCustomerOrder(StockTO to);

	Map<String, Object> isSameCustomer(String stocksno);
}
