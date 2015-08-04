package com.hydom.account.service;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.hydom.account.ebean.MemberCoupon;
import com.hydom.util.bean.MemberBean;
import com.hydom.util.dao.DAOSupport;

@Service
public class MemberCouponServiceBean extends DAOSupport<MemberCoupon> implements MemberCouponService {
	
	@Override
	public MemberCoupon getCoupon(MemberBean bean, String couponId) {
		String jqpl = "form MemberCoupon o where 1=1 and o.coupon.id=:couponId and o.member.id=:memberId";
		Query query = em.createQuery(jqpl,MemberCoupon.class);
		query.setParameter("couponId", couponId);
		query.setParameter("memberId", bean.getMember().getId());
		return (MemberCoupon) query.getSingleResult();
	}
	
}
