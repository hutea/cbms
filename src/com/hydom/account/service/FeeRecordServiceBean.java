package com.hydom.account.service;

import org.springframework.stereotype.Service;

import com.hydom.account.ebean.FeeRecord;
import com.hydom.util.dao.DAOSupport;

@Service
public class FeeRecordServiceBean extends DAOSupport<FeeRecord> implements
		FeeRecordService {

}
