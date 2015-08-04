package com.hydom.core.server.service;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Service;

import com.hydom.core.server.ebean.Car;
import com.hydom.util.dao.DAOSupport;

/**
 * @Description 车型业务层实现
 * @author WY
 * @date 2015年7月1日 下午5:46:26
 */

@Service
public class CarServiceBean extends DAOSupport<Car> implements CarService{

	@Override
	public boolean isExist(String name) {
		Car car = null;
		try {
			car = (Car) em.createQuery("select o from Car o where o.visible=1 and o.name=?1")
			.setParameter(1, name).getSingleResult();
		} catch (NoResultException e) {
		}
		if(car==null){
			return false;
		}else{
			return true;
		}
	}

}
