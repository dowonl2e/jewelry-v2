package com.jewelry.catalog.service;

import com.jewelry.catalog.domain.CatalogTO;
import com.jewelry.catalog.domain.CatalogVO;

import java.util.Map;

public interface CatalogService {
	
	Map<String, Object> findAllCatalog(CatalogTO to);
		
	CatalogVO findCatalogByNo(CatalogTO to);
	
	String insertCatalog(CatalogTO to);
	
	String updateCatalog(CatalogTO to);
	
	String updateCatalogToDelete(String cdid);
	
	String updateCatalogsToDelete(CatalogTO to);
	
}
