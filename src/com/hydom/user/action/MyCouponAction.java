package com.hydom.user.action;

import java.util.Date;
import java.util.LinkedHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hydom.account.ebean.Member;
import com.hydom.account.ebean.MemberCoupon;
import com.hydom.account.service.MemberCouponService;
import com.hydom.account.service.MemberService;
import com.hydom.core.server.ebean.Coupon;
import com.hydom.core.server.service.CouponService;
import com.hydom.util.BaseAction;
import com.hydom.util.dao.PageView;

/**
 * @Description 我的优惠券控制层
 * @author WY
 * @date 2015年7月31日 下午2:53:17
 */

@RequestMapping("/user/myCoupon")
@Controller
public class MyCouponAction extends BaseAction{

	@Resource
	private MemberCouponService memberCouponService;
	@Resource
	private CouponService couponService;
	@Resource
	private MemberService memberService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	private int maxresult = 5;
	/**优惠券列表*/
	@RequestMapping("/list")
	public @ResponseBody ModelAndView list(String memberId) {
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("type", "desc");
		String jpql = "o.visible=?1";
		Object[] params = new Object[]{true};
		
		ModelAndView mav = new ModelAndView("web/center/myCoupon");
		if(memberId!=null){
			mav.addObject("amount", memberService.find(memberId).getAmount());
		}else{
			mav.addObject("amount", "请登录");
		}
		mav.addObject("coupons", couponService.getScrollData(-1, -1, jpql, params, orderby).getResultList());
		return mav;
	}
	
	/**兑换优惠券*/
	@RequestMapping("/convert")
	public @ResponseBody String convert(String id, String memberId) {
		Member member = memberService.find(memberId);
		Coupon coupon = couponService.find(id);
		if(coupon.getIsExchange()){
			if(coupon.getPoint()<=member.getAmount()){
				MemberCoupon memberCoupon = new MemberCoupon();
				memberCoupon.setReceiveDate(new Date());
				memberCoupon.setMember(member);
				memberCoupon.setCoupon(coupon);
				memberCouponService.save(memberCoupon);
				member.setAmount(member.getAmount()-coupon.getPoint());
				memberService.update(member);
				return ajaxSuccess("领取成功！", response);
			}else{
				return ajaxError("积分不足！", response);
			}
		}
		return ajaxError("领取失败！", response);
	}
	
	/**优惠券历史记录*/
	@RequestMapping("/history")
	public @ResponseBody ModelAndView history(String memberId,@RequestParam(defaultValue="1") Integer page) {
		
		PageView<MemberCoupon> pageView = new PageView<MemberCoupon>(maxresult, page);
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		String jpql = "o.member.id = ?1";
		Object[] params = new Object[]{memberId};
		pageView.setHql(jpql);
		pageView.setParams(params);
		pageView.setOrderby(orderby);
		pageView = memberCouponService.getPage(pageView);
		//pageView.setQueryesRult(memberCouponService.getScrollData(pageView.getFirstResult(), maxresult, jpql, params, orderby));
		//request.setAttribute("pageView", pageView);
		
		ModelAndView mav = new ModelAndView("web/center/myCoupon_history");
		mav.addObject("pageView", pageView);
		return mav;
	}
}
