package com.hydom.core.server.service;

import java.util.List;

import com.hydom.core.server.ebean.CarType;
import com.hydom.util.dao.DAO;

/**
 * @Description:车系业务层接口
 * @author WY
 * @date 2015年7月1日 上午10:01:02
 */

public interface CarTypeService extends DAO<CarType> {

	/**
	 * 根据车系名称查询品牌是否存在
	 * 
	 * @param name
	 *            名称
	 * @return true存在 false不存在
	 */
	public boolean isExist(String name);

	/**
	 * 根据顶级车系ID获取下属子车系列表
	 * 
	 * @param id
	 *            顶级车系ID
	 * @return
	 */
	public List<CarType> listByTopID(String id);
}
