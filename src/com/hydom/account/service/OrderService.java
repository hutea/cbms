package com.hydom.account.service;

import java.util.Date;
import java.util.List;

import com.hydom.account.ebean.Area;
import com.hydom.account.ebean.Order;
import com.hydom.account.ebean.ServiceType;
import com.hydom.util.bean.DateMapBean;
import com.hydom.util.dao.DAO;

public interface OrderService extends DAO<Order> {

	/**
	 * 给指定的订单绑定技师<br>
	 * 绑定原则：<br>
	 * <1>根据用户经纬度由近及远绑定；<br>
	 * <2>用户没有绑定过此订单（订单不分配给拒绝过技师）<br>
	 * <3>技师状态为上班且空闲状态<br>
	 * 主要功能及说明：<br>
	 * <1>技师绑定记录与订单绑定在同一事务中进行<br>
	 * <2>绑定时会时行相应距离设置<br>
	 * <3>绑定成功返回true时应执行相应的推送<br>
	 * 
	 * @param oid
	 * @return
	 */
	public boolean bindTechnician(String oid);

	/**
	 * 根据区域 和 时间 获取 这个区域内在这段时间内的所有空闲时间段
	 * 
	 * @param area
	 * @param nowDate
	 * @return
	 */
	public List<DateMapBean> getDateMapBean(String areaId, Date nowDate,
			String serviceTypeId);

	/**
	 * 根据时间断 获取该服务是否存在可以利用的车队
	 * 
	 * @param startDate
	 *            服务开始时间 2015-06-06 12:00:00
	 * @param endDate
	 *            服务结束时间 2015-06-06 13:00:00
	 * @param area
	 *            服务地区
	 * @return
	 */
	public boolean checkDateTime(Date startDate, Date endDate, Area area);

	/**
	 * 根据当前订单 查询同时间段内 所有已被绑定的订单
	 * 
	 * @param order
	 * @return
	 */
	public List<Order> getBindingOrder(Order order);

}