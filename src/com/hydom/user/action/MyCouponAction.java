package com.hydom.user.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hydom.account.service.MemberCouponService;
import com.hydom.core.server.service.CarService;
import com.hydom.core.server.service.CouponService;
import com.hydom.util.BaseAction;

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
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	/**显示*/
	@RequestMapping("/list")
	public @ResponseBody ModelAndView list() {
		ModelAndView mav = new ModelAndView("web/center/myCoupon");
		mav.addObject("coupoms", couponService.getScrollData().getResultList());
		return mav;
	}
}
