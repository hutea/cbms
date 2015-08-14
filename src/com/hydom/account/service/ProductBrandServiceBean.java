package com.hydom.account.service;

import org.springframework.stereotype.Service;

import com.hydom.account.ebean.ProductBrand;
import com.hydom.util.dao.DAOSupport;

@Service
public class ProductBrandServiceBean extends DAOSupport<ProductBrand> implements
		ProductBrandService {

	@Override
	public ProductBrand findOneRecommendBrand() {
		try {
			return (ProductBrand) em
					.createQuery(
							"select o from ProductBrand  o where o.visible=?1 and o.commandBrand=?2 order by o.modifyDate desc")
					.setParameter(1, true).setParameter(2, 1).setMaxResults(1)
					.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
