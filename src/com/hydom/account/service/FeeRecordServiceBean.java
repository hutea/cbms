package com.hydom.account.service;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.hydom.account.ebean.FeeRecord;
import com.hydom.util.dao.DAOSupport;

@Service
public class FeeRecordServiceBean extends DAOSupport<FeeRecord> implements
		FeeRecordService {

	@SuppressWarnings("unchecked")
	@Override
	public FeeRecord findByRechargeNum(String tradeNum) {
		String sql = "select o from FeeRecord o where o.rechargeNo=:rechargeNo and o.visible=:visible";
		Query query = em.createQuery(sql);
		query.setParameter("rechargeNo", tradeNum);
		query.setParameter("visible", false);
		List<FeeRecord> list = query.getResultList();
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}

}
