package com.hydom.user.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hydom.account.service.MemberService;
import com.hydom.core.server.service.CarService;
import com.hydom.user.ebean.UserCar;
import com.hydom.user.service.UserCarService;
import com.hydom.util.BaseAction;

/**
 * @Description 车管家控制层
 * @author WY
 * @date 2015年7月28日 下午4:49:04
 */

@RequestMapping("/user/carSteward")
@Controller
public class UserCarAction extends BaseAction{
	
	@Resource
	private CarService carService;
	@Resource
	private UserCarService userCarService;
	@Resource
	private MemberService memberService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	/**增加*/
	@RequestMapping("/add")
	public @ResponseBody ModelAndView add() {
		ModelAndView mav = new ModelAndView("web/center/carSteward_add");
		return mav;
	}
	
	/**保存*/
	@RequestMapping("/save")
	public @ResponseBody ModelAndView save(ModelMap model, UserCar userCar, String carId) {
		if(StringUtils.isEmpty(userCar.getId())){
			userCar.setCar(carService.find(carId));
			userCar.setMember(memberService.getScrollData().getResultList().get(0));
			userCarService.save(userCar);
		}else{
			userCar.setCar(carService.find(carId));
			userCar.setMember(memberService.getScrollData().getResultList().get(0));
			userCarService.update(userCar);
		}
		ModelAndView mav = new ModelAndView("redirect:list");
		return  mav;
	}
	
	/**显示*/
	@RequestMapping("/list")
	public @ResponseBody ModelAndView list() {
		ModelAndView mav = new ModelAndView("web/center/carSteward_list");
		mav.addObject("userCars", userCarService.getScrollData().getResultList());
		return mav;
	}
	
	/**修改*/
	@RequestMapping("/update")
	public @ResponseBody ModelAndView update(String userCarId) {
		ModelAndView mav = new ModelAndView("web/center/carSteward_add");
		mav.addObject("userCar", userCarService.find(userCarId));
		return mav;
	}
}
