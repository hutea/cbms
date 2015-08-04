package com.hydom.core.web.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hydom.account.ebean.Area;
import com.hydom.account.ebean.MemberCoupon;
import com.hydom.account.ebean.Order;
import com.hydom.account.ebean.ServiceType;
import com.hydom.account.service.AreaService;
import com.hydom.account.service.MemberCouponService;
import com.hydom.account.service.MemberService;
import com.hydom.account.service.OrderService;
import com.hydom.account.service.ServiceTypeService;
import com.hydom.core.server.ebean.Car;
import com.hydom.core.server.service.CarService;
import com.hydom.user.ebean.UserCar;
import com.hydom.util.BaseAction;
import com.hydom.util.CommonAttributes;
import com.hydom.util.CommonUtil;
import com.hydom.util.IDGenerator;
import com.hydom.util.bean.MemberBean;
import com.hydom.util.bean.UserCarBean;
import com.hydom.util.cache.CachedManager;

/**
 * web 2级页面处理
 * @author liudun
 *
 */

@RequestMapping("/web")
@Controller
public class Index2Action extends BaseAction{
	
	private static final String base = "/index";
	
	@Resource
	private MemberService memberService;
	@Resource
	private ServiceTypeService serviceTypeService;
	@Resource
	private AreaService areaService;
	@Resource
	private OrderService orderService;
	@Resource
	private CarService carService;
	@Resource
	private MemberCouponService memberCouponService;
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	/**
	 * 添加订单
	 */
	@RequestMapping("/addOrder")
	public String addOrder(ModelMap model,String carId,String serviceTypeId) {
		
		Car car = carService.find(carId);
		model.put("car", car);
		
		MemberBean memberBean = getMemberBean(request);
		model.put("member", memberBean);
		
		ServiceType serviceType = serviceTypeService.find(serviceTypeId);
		model.put("serviceType", serviceType);
		
		String jpql = "o.parent is null and o.visible = true";
		List<Area> areas= areaService.getList(jpql, null, null);
		model.put("areas", areas);
		
		return base+"/maintain3";
	}
	
	/**	             以下是添加保养服务                              */
	
	/**
	 * 点击上面保养 
	 * @param model
	 * @param serviceTypeId
	 * @return
	 */
	@RequestMapping("/chooseService")
	public String chooseService(ModelMap model){
		
		MemberBean memberBean = getMemberBean(request);
		if(memberBean == null || memberBean.getDefaultCar() == null){
			return base+"/server_chooseCar";
		}
		
		UserCar userCar = memberBean.getDefaultCar();
		//将 cleanCarAddOrder 方法返回的参数统一
		UserCarBean bean = new UserCarBean(userCar.getCarNum(),userCar.getCarColor(),userCar.getCar());
		model.addAttribute("carServer", bean);
		
		List<ServiceType> serviceType = serviceTypeService.getServiceType(2);
		model.addAttribute("serviceType", serviceType);
		
		model.addAttribute("member", getMemberBean(request));
		
	/*	List<ServiceType> serviceTypes = serviceTypeService.getServiceType(2);
		model.put("serviceTypes", serviceTypes);
		
		Car car = carService.find(carId);
		model.put("car", car);*/
		
		return base + "/server_chooseServer";
	}
	/**
	 * 去保养 跳转接口
	 * @param model
	 * @param carId
	 * @return
	 */
	@RequestMapping("/gotoService")
	public String gotoService(ModelMap model,String carId){
		Car car = carService.find(carId);
		UserCarBean bean = new UserCarBean(null,null,car);
		System.out.println(bean.getCar().getCarBrand().getImgPath());
		System.out.println(bean.getCar().getName());
		model.addAttribute("carServer", bean);
		
		List<ServiceType> serviceTypes = serviceTypeService.getServiceType(1);
		model.addAttribute("serviceTypes", serviceTypes);
		
		model.addAttribute("member", getMemberBean(request));
		return base + "/server_chooseServer";
	}
	
	
	
	/*@RequestMapping("/getTimeMap")
	@ResponseBody
	public String getTimeMap(String areaId,@RequestParam Date date,String serviceTypeId){
		List<DateMapBean> beans = orderService.getDateMapBean(areaId, date, serviceTypeId);
		JSONArray array = new JSONArray();
		for(DateMapBean bean : beans){
			if(bean.getCarTeamCount() > 0){//车队服务数量大于0时  返回该时间段
				JSONObject obj = new JSONObject();
				obj.put("date", bean.getMapDate());
				obj.put("dateMap", bean.getMap());
				obj.put("count", bean.getCarTeamCount());
				array.add(obj);
			}
		}
		return ajaxSuccess(array, response);
	}*/
	/**	              添加保养服务结束                     */
	/**       以下是添加洗车预约                */
	
	/**
	 * 用户存在 判断是否含有默认车型 含有默认车型 则直接跳转到订单选择
	 * 用户不存在 或者 用户存在 不存在默认车型   跳转到选择车型页面
	 * @param model
	 * @param carId
	 * @return
	 */
	@RequestMapping("/cleanCarService")
	public String cleanCarService(ModelMap model){
		MemberBean memberBean = getMemberBean(request);
		if(memberBean == null || memberBean.getDefaultCar() == null){
			return base+"/cleanCar_chooseCar";
		}
		UserCar userCar = memberBean.getDefaultCar();
		//将 cleanCarAddOrder 方法返回的参数统一
		UserCarBean bean = new UserCarBean(userCar.getCarNum(),userCar.getCarColor(),userCar.getCar());
		model.addAttribute("cleanCar", bean);
		
		ServiceType serviceType = serviceTypeService.find(CommonAttributes.getInstance().getCleanCar());
		model.addAttribute("serviceType", serviceType);
		
		model.addAttribute("member", getMemberBean(request));
		
		return base+"/cleanCar_addOrder";
	}
	
	/**
	 * 由洗车服务 选择 车型后 跳转到订单生成页面
	 * @param model
	 * @param carId
	 * @return
	 */
	@RequestMapping("/cleanCarAddOrder")
	public String cleanCarAddOrder(ModelMap model,String carId){
		
		Car car = carService.find(carId);
		UserCarBean bean = new UserCarBean(null,null,car);
		model.addAttribute("cleanCar", bean);
		
		ServiceType serviceType = serviceTypeService.find(CommonAttributes.getInstance().getCleanCar());
		model.addAttribute("serviceType", serviceType);
		
		model.addAttribute("member", getMemberBean(request));
		
		return base+"/cleanCar_addOrder";
	}

	
	/** 洗车流程
	 * 1、添加洗车预约
	 * 2、确定预约信息 
	 * 3、保存到数据库 并派技师服务
	 */
	/**
	 * 生成订单  =====> 跳转到确认订单页面
	 * @param order
	 * @return
	 */
	@RequestMapping("/existOrder")
	public String existOrder(Order order,String couponId){
		
		try {
			
			Car car = carService.find(order.getCar().getId());
			order.setCar(car);
			
			//ServiceType serviceType = serviceTypeService.find(order.getServiceType().getId());
			//order.setType(serviceType.getType());
			
			if(StringUtils.isNotEmpty(couponId)){
				MemberBean bean = getMemberBean(request);
				MemberCoupon couponMember = memberCouponService.getCoupon(bean,couponId);
				order.setMemberCoupon(couponMember);
			}
			
			order.setStatus(1);
			
			Float money = order.getAmount_money();
			Float paid = order.getAmount_paid();
			Float price = CommonUtil.subtract(money+"", paid+"");
			order.setPrice(price);
		
			//将Order放入缓存
			String orderCachedId = IDGenerator.getRandomString(32, 0);
			CachedManager.putObjectCached("order", orderCachedId, order);
			
			return "redirect:gotoConfirm?confimId="+orderCachedId;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ERROR;
	}
	
	/**
	 * 跳转到确认订单页面
	 * @param confimId
	 * @return
	 */
	@RequestMapping("/gotoConfirm")
	public String gotoConfirm(String confimId,ModelMap model){
		
		try {
			Order order = (Order) CachedManager.getObjectCached("order", confimId);
			model.addAttribute("order", order);
			model.addAttribute("confimId", confimId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return base + "/cleanCar_confirmOrder";
	}
	
	/**
	 * 生成订单 ====》 保存数据库
	 * @param confimId
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public String save(String confimId){
		
		try {
			Order order = (Order) CachedManager.getObjectCached("order", confimId);
			orderService.save(order);
			CachedManager.remove("order", confimId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ajaxSuccess("成功", response);
	}
	
	/**
	 * 生成订单 ====》 保存数据库
	 * @param confimId
	 * @return
	 */
	@RequestMapping("/modify")
	@ResponseBody
	public String modify(String confimId){
		
		try {
			CachedManager.remove("order", confimId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ajaxSuccess("成功", response);
	}
}
