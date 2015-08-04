package com.hydom.account.service;

import java.util.List;

import com.hydom.account.ebean.ProductCategory;
import com.hydom.util.dao.DAO;

public interface ProductCategoryService extends DAO<ProductCategory> {

	List<ProductCategory> findProductCategory(ProductCategory productCategory);
	
	List<ProductCategory> findChildren(ProductCategory productCategory, Integer count);
}
