package com.hydom.core.server.service;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Service;

import com.hydom.core.server.ebean.CarBrand;
import com.hydom.util.dao.DAOSupport;

/**
 * @Description:汽车品牌业务层实现
 * @author WY
 * @date 2015年6月26日 下午5:33:32
 */

@Service
public class CarBrandServiceBean extends DAOSupport<CarBrand> implements CarBrandService{

	@Override
	public boolean isExist(String name) {
		CarBrand carBrand = null;
		try {
			carBrand = (CarBrand) em.createQuery("select o from CarBrand o where o.visible=1 and o.name=?1")
			.setParameter(1, name).getSingleResult();
		} catch (NoResultException e) {
		}
		if(carBrand==null){
			return false;
		}else{
			return true;
		}
		
	}

}
