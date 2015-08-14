package com.hydom.account.service;

import java.util.List;

import com.hydom.account.ebean.MemberCoupon;
import com.hydom.util.bean.MemberBean;
import com.hydom.util.dao.DAO;

public interface MemberCouponService extends DAO<MemberCoupon> {

	MemberCoupon getCoupon(MemberBean bean, String couponId);

	/**
	 * 根据memberId 获取该用户所领取的优惠卷
	 * 
	 * @param id
	 * @return
	 */
	List<MemberCoupon> getCouponByMember(String id);

	/**
	 * 兑换优惠券
	 * 
	 * @param uid
	 *            用户ID
	 * @param cpid
	 *            优惠券ID
	 * @return 1=兑换成功、0=积分不足、-1=有异常
	 */
	public int exchange(String uid, String cpid);

	/**
	 * 获取用户可用的优惠券列表
	 * 
	 * @param pay
	 *            订单金额
	 * @param pids
	 *            选择的产品ID数组[没产品传null即可]
	 * @return
	 */
	public List<MemberCoupon> canUseList(double pay, String memberId,
			String[] pids);

	/**
	 * 是否有可用的优惠券
	 * 
	 * @param pay
	 *            订单金额
	 * @param pids
	 *            选择的产品ID数组[没产品传null即可]
	 * @return
	 */
	public boolean canUse(double pay, String memberId, String[] pids);

}
