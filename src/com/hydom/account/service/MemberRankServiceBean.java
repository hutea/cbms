package com.hydom.account.service;

import org.springframework.stereotype.Service;

import com.hydom.account.ebean.MemberRank;
import com.hydom.util.dao.DAOSupport;

@Service
public class MemberRankServiceBean extends DAOSupport<MemberRank> implements MemberRankService {

}
