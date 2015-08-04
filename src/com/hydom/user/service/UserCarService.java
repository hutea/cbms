package com.hydom.user.service;

import com.hydom.user.ebean.UserCar;
import com.hydom.util.dao.DAO;

public interface UserCarService extends DAO<UserCar> {

	/***
	 * 重置指定用户所有车为非默认车
	 * 
	 * @param uid
	 */
	public void resetUndefault(String uid);

	/**
	 * 得到用户的默认车辆信息
	 * 
	 * @param uid
	 * @return
	 */
	public UserCar defaultCar(String uid);
}
