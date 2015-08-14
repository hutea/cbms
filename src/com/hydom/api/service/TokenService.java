package com.hydom.api.service;

import com.hydom.api.ebean.Token;
import com.hydom.util.dao.DAO;

public interface TokenService extends DAO<Token> {

	/***
	 * 通过用户ID和令牌值获取Token实体
	 * 
	 * @param uid
	 * @param authId
	 * @return
	 */
	public Token findToken(String uid, String authId);

}
