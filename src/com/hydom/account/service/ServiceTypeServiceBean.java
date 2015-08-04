package com.hydom.account.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hydom.account.ebean.ServiceType;
import com.hydom.util.dao.DAOSupport;

@Service
public class ServiceTypeServiceBean extends DAOSupport<ServiceType> implements ServiceTypeService {
	
	@Override
	public List<ServiceType> getServiceType(Integer i) {
		
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		String jpql = "type=?1 and visible=true";
		List<Object> params = new ArrayList<Object>();
		params.add(i);
		return this.getList(jpql, params.toArray(), orderby);
	}
	
	
}
