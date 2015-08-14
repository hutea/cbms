package com.hydom.account.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hydom.account.ebean.Member;
import com.hydom.account.ebean.MemberCoupon;
import com.hydom.account.ebean.Product;
import com.hydom.core.server.ebean.Coupon;
import com.hydom.core.server.service.CouponService;
import com.hydom.util.bean.MemberBean;
import com.hydom.util.dao.DAOSupport;

@Service
public class MemberCouponServiceBean extends DAOSupport<MemberCoupon> implements
		MemberCouponService {
	@Resource
	private CouponService couponService;
	@Resource
	private MemberService memberService;
	@Resource
	private ProductService productService;

	@SuppressWarnings("unchecked")
	@Override
	public List<MemberCoupon> canUseList(double pay, String memberId,
			String[] pids) {
		if (pids != null && pids.length > 0) { // 检查产品是否支持使用优惠券
			for (String pid : pids) {
				Product product = productService.find(pid);
				if (product.getUseCoupon() == null
						|| product.getUseCoupon() != 0) { // 0可以使用
					return null;
				}
			}
		}
		Date now = new Date();
		// 起始日期<=当前日期；结束日期>=当前日期（或为null值的无限期）；未使用；
		return em
				.createQuery(
						"select o from  MemberCoupon o where o.beginDate<=?1 and (o.endDate is null or o.endDate>=?2) and o.status=?3 and o.member.id=?4 and ((o.type=1 and o.minPrice=?5) or (o.type=2 and o.minPrice=?6) or o.type=3)")
				.setParameter(1, now).setParameter(2, now).setParameter(3, 0)
				.setParameter(4, memberId).setParameter(5, pay)
				.setParameter(6, pay).getResultList();

	}

	@Override
	public boolean canUse(double pay, String memmberId, String[] pids) {
		if (pids != null && pids.length > 0) { // 检查产品是否支持使用优惠券
			for (String pid : pids) {
				Product product = productService.find(pid);
				if (product.getUseCoupon() == null
						|| product.getUseCoupon() != 0) { // 0可以使用
					return false;
				}
			}
		}
		Date now = new Date();
		// 起始日期<=当前日期；结束日期>=当前日期（或为null值的无限期）；未使用；
		Long count = (Long) em
				.createQuery(
						"select count(o.id) from  MemberCoupon o where o.beginDate<=?1 and (o.endDate is null or o.endDate>=?2) and o.status=?3 and o.member.id=?4  and ((o.type=1 and o.minPrice=?5) or (o.type=2 and o.minPrice=?6) or o.type=3)")
				.setParameter(1, now).setParameter(2, now).setParameter(3, 0)
				.setParameter(4, memmberId).setParameter(5, pay)
				.setParameter(6, pay).getSingleResult();
		if (count > 0) {
			return true;

		} else {
			return false;
		}

	}

	@Override
	public MemberCoupon getCoupon(MemberBean bean, String couponId) {
		String jqpl = "form MemberCoupon o where 1=1 and o.coupon.id=:couponId and o.member.id=:memberId";
		Query query = em.createQuery(jqpl, MemberCoupon.class);
		query.setParameter("couponId", couponId);
		query.setParameter("memberId", bean.getMember().getId());
		return (MemberCoupon) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MemberCoupon> getCouponByMember(String id) {
		String hql = "select mc from MemberCoupon mc where mc.member.id = :memberId and (mc.coupon.endDate >= :nowDate or mc.coupon.endDate is null) and mc.status = 0 and mc.coupon.visible = :visible order by mc.createDate";
		Query query = em.createQuery(hql);
		query.setParameter("memberId", id);
		query.setParameter("nowDate", new Date());
		query.setParameter("visible", true);
		List<MemberCoupon> list = query.getResultList();
		return list;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public int exchange(String uid, String cpid) {
		try {
			Member member = memberService.find(uid);
			Coupon coupon = couponService.find(cpid);
			System.out.println(member);
			System.out.println(coupon);
			int requireScore = (coupon.getPoint() == null ? 0 : coupon
					.getPoint());
			if (member.getAmount() >= requireScore) {// 可以兑换
				MemberCoupon memberCoupon = new MemberCoupon();
				memberCoupon.setBeginDate(coupon.getBeginDate());
				memberCoupon.setCoupon(coupon);
				memberCoupon.setDiscount(coupon.getDiscount());
				memberCoupon.setRate(coupon.getRate());
				memberCoupon.setMinPrice(coupon.getMinPrice());
				memberCoupon.setImgPath(coupon.getImgPath());
				this.save(memberCoupon);
				member.setAmount(member.getAmount() - requireScore);
				memberService.update(member);
				return 1;
			} else {// 积分不足
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
