package com.hydom.user.action;

import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hydom.account.service.MemberService;
import com.hydom.core.server.service.CarService;
import com.hydom.user.ebean.UserCar;
import com.hydom.user.service.UserCarService;
import com.hydom.util.BaseAction;
import com.hydom.util.bean.UserCarBean;

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
	public @ResponseBody ModelAndView save(UserCar userCar, String carId, String memberId) {
		if(StringUtils.isEmpty(userCar.getId())){
			userCar.setCar(carService.find(carId));
			userCar.setMember(memberService.getScrollData().getResultList().get(0));
			userCarService.save(userCar);
		}else{
			userCar.setCar(carService.find(carId));
			userCar.setMember(memberService.getScrollData().getResultList().get(0));
			userCarService.update(userCar);
		}
		ModelAndView mav = new ModelAndView("redirect:list?memberId="+memberId);
		return  mav;
	}
	
	/**显示*/
	@RequestMapping("/list")
	public @ResponseBody ModelAndView list(String memberId) {
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		String jpql = "o.member.id=?1";
		Object[] params = new Object[]{memberId};
		ModelAndView mav = new ModelAndView("web/center/carSteward_list");
		mav.addObject("userCars", userCarService.getScrollData(-1, -1, jpql, params, orderby).getResultList());
		return mav;
	}
	
	/**修改*/
	@RequestMapping("/update")
	public @ResponseBody ModelAndView update(String userCarId) {
		ModelAndView mav = new ModelAndView("web/center/carSteward_add");
		mav.addObject("userCar", userCarService.find(userCarId));
		return mav;
	}
	
	/**设为默认车型*/
	@RequestMapping("/setDefaultCar")
	public @ResponseBody String setDefaultCar(String userCarId) {
		try {
			List<UserCar> userCars = userCarService.getScrollData().getResultList();
			for(int i=0;i<userCars.size();i++){
				if(userCars.get(i).getId().equals(userCarId)){
					userCars.get(i).setDefaultCar(true);
					userCarService.update(userCars.get(i));
				}else{
					if(!userCars.get(i).getDefaultCar().equals(false)){
						userCars.get(i).setDefaultCar(false);
						userCarService.update(userCars.get(i));
					}
				}
			}
			return ajaxSuccess("设置成功！", response);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxError("设置失败！", response);
		}
	}
	
	/**删除*/
	@RequestMapping("/del")
	public @ResponseBody String del(String userCarId, String memberId) {
		try {
			UserCar uc = userCarService.find(userCarId);
			if(uc.getDefaultCar()==true){
				if(userCarId!=null){
					userCarService.delete(userCarId);
				}
				LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
				orderby.put("createDate", "desc");
				String jpql = "o.member.id=?1";
				Object[] params = new Object[]{memberId};
				uc = userCarService.getScrollData(-1, -1, jpql, params, orderby).getResultList().get(0);
				uc.setDefaultCar(true);
				userCarService.update(uc);
			}
			return ajaxSuccess("删除成功！", response);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxError("删除失败！", response);
		}
	}
}
