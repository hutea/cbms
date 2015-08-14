package com.hydom.account.service;

import java.util.List;

import com.hydom.account.ebean.ProductCategory;
import com.hydom.util.dao.DAO;

public interface ProductCategoryService extends DAO<ProductCategory> {

	List<ProductCategory> findProductCategory(ProductCategory productCategory);

	List<ProductCategory> findChildren(ProductCategory productCategory,
			Integer count);

	/**
	 * 根据父分类ID 得到所有子类ID
	 * 
	 * @param parentids
	 *            父类ID字串数组
	 * @return
	 */
	public List<String> getSubTypeid(String[] parentids);

	/**
	 * 根据父分类 得到所有直接子类列表
	 * 
	 * @param parentId
	 * @return
	 */
	List<ProductCategory> listChildCategory(String parentId);
}
