package com.hydom.core.server.service;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Service;

import com.hydom.core.server.ebean.CarType;
import com.hydom.util.dao.DAOSupport;

/**
 * @Description 车系业务层实现
 * @author WY
 * @date 2015年7月1日 上午10:02:26
 */

@Service
public class CarTypeServiceBean extends DAOSupport<CarType> implements
		CarTypeService {

	@Override
	public boolean isExist(String name) {
		CarType carType = null;
		try {
			carType = (CarType) em
					.createQuery(
							"select o from CarType o where o.visible=1 and o.name=?1")
					.setParameter(1, name).getSingleResult();
		} catch (NoResultException e) {
		}
		if (carType == null) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CarType> listByTopID(String id) {
		return em
				.createQuery(
						"select o from CarType o where o.visible=?1 and o.parent.id=?2 and o.level=?3")
				.setParameter(1, true).setParameter(2, id).setParameter(3, 2)
				.getResultList();
	}

}
