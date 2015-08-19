package com.hydom.account.service;

import com.hydom.account.ebean.FeeRecord;
import com.hydom.util.dao.DAO;

public interface FeeRecordService extends DAO<FeeRecord> {
	
	/**
	 * 根据充值比编号 查找 充值记录
	 * @param tradeNum
	 * @return
	 */
	FeeRecord findByRechargeNum(String tradeNum);

}
