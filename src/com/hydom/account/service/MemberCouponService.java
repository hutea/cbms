package com.hydom.account.service;

import com.hydom.account.ebean.MemberCoupon;
import com.hydom.util.bean.MemberBean;
import com.hydom.util.dao.DAO;

public interface MemberCouponService extends DAO<MemberCoupon> {

	MemberCoupon getCoupon(MemberBean bean, String couponId);

}
