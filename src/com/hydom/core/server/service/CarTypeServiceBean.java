package com.hydom.core.server.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hydom.account.ebean.Product;
import com.hydom.core.server.ebean.Car;
import com.hydom.core.server.ebean.CarBrand;
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
	
	@SuppressWarnings("unchecked")
	@Override
	public String getChooseCarType(Product product) {
	/*	String hql = "select o from CarType o join o.carList c where c in(:carSet)";
		
		Query query = em.createQuery(hql);
		query.setParameter("carSet", product.getCarSet());
		List<CarType> carBrands = query.getResultList();*/
		
		Set<Car> carSet = product.getCarSet();
		
		Set<CarType> CarTypes = new HashSet<CarType>();
		for(Car car : carSet){
			CarTypes.add(car.getCarType());
		}
		
		JSONArray array = new JSONArray();
		for(CarType cb : CarTypes){
			JSONObject obj = new JSONObject();
			obj.put("id", cb.getId());
			obj.put("parentId", cb.getCarBrand().getId());
			array.add(obj);
		}
		
		/*String brandIds = "";
		for(CarType cb : carBrands){
			if(StringUtils.isNotEmpty(brandIds)){
				brandIds+=",";
			}
			brandIds += cb.getId();
		}*/
		return array.toString();
	}

}
