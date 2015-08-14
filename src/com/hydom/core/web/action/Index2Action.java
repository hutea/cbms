package com.hydom.core.web.action;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hydom.account.ebean.Area;
import com.hydom.account.ebean.Member;
import com.hydom.account.ebean.MemberCoupon;
import com.hydom.account.ebean.Order;
import com.hydom.account.ebean.Product;
import com.hydom.account.ebean.ServerOrder;
import com.hydom.account.ebean.ServerOrderDetail;
import com.hydom.account.ebean.ServiceType;
import com.hydom.account.service.AreaService;
import com.hydom.account.service.MemberCouponService;
import com.hydom.account.service.MemberService;
import com.hydom.account.service.OrderService;
import com.hydom.account.service.ServerOrderDetailService;
import com.hydom.account.service.ServerOrderService;
import com.hydom.account.service.ServiceTypeService;
import com.hydom.account.service.TechnicianService;
import com.hydom.core.server.ebean.Car;
import com.hydom.core.server.ebean.CarBrand;
import com.hydom.core.server.ebean.CarType;
import com.hydom.core.server.ebean.Coupon;
import com.hydom.core.server.service.CarService;
import com.hydom.core.server.service.CouponService;
import com.hydom.user.ebean.UserCar;
import com.hydom.user.service.UserCarService;
import com.hydom.util.BaseAction;
import com.hydom.util.CommonAttributes;
import com.hydom.util.CommonUtil;
import com.hydom.util.DateTimeHelper;
import com.hydom.util.IDGenerator;
import com.hydom.util.bean.DateMapBean;
import com.hydom.util.bean.MemberBean;
import com.hydom.util.bean.ServerOrderBean;
import com.hydom.util.bean.ServerOrderBean.ServerOrderProductBean;
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
	@Resource
	private ServerOrderService serverOrderService;
	@Resource
	private ServerOrderDetailService detailService;
	@Resource
	private TechnicianService technicianService;
	@Resource
	private CouponService couponService;
	@Resource
	private UserCarService userCarService;
	
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
		
		UserCar userCar = userCarService.find(memberBean.getDefaultCar().getId());
		//将 cleanCarAddOrder 方法返回的参数统一
		UserCarBean bean = new UserCarBean(userCar.getCarNum(),userCar.getCarColor(),userCar.getCar(),userCar);
		model.addAttribute("cleanCar", bean);
		
		List<ServiceType> serviceTypes = serviceTypeService.getServiceType(1);
		model.addAttribute("serviceTypes", serviceTypes);
		
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
		
		MemberBean memberBean = getMemberBean(request);
		
		if(memberBean!=null){
			UserCar userCar = userCarService.find(memberBean.getDefaultCar().getId());
			//将 cleanCarAddOrder 方法返回的参数统一
			UserCarBean bean = new UserCarBean(userCar.getCarNum(),userCar.getCarColor(),userCar.getCar(),userCar);
			model.addAttribute("cleanCar", bean);
		}else{
			Car car = carService.find(carId);
			UserCarBean bean = new UserCarBean(null,null,car,null);
			model.addAttribute("cleanCar", bean);
		}
		
		List<ServiceType> serviceTypes = serviceTypeService.getServiceType(1);
		model.addAttribute("serviceTypes", serviceTypes);
		
		model.addAttribute("member", memberBean);
		return base + "/server_chooseServer";
	}
	
	/**
	 * 添加serverOrder
	 * @param model
	 * @param content 服务商品
	 * @param chooseServer 是否选择服务 1选择 0没选中
	 * @param memberCouponId 用户优惠卷ID
	 * @param carId 车辆ID
	 * @param userCarId 用户车管家ID
	 * @param carColor 车辆颜色
	 * @param carNum 车牌号
	 * @return
	 */
	@RequestMapping("/serverAddOrder")
	public String serverAddOrder(ModelMap model,String content,
			Integer chooseServer,String memberCouponId,
			String carId,String userCarId,String carColor,String carNum){
		
		model.put("content", content);//选择商品 或者 服务
		model.put("chooseServer", chooseServer); //是否选择服务
		model.put("memberCouponId", memberCouponId);//用户的优惠卷
		model.put("carId", carId);//车辆ID
		model.put("userCarId", userCarId);//用户车管家ID
		model.put("carColor", carColor);//车辆颜色
		model.put("carNum", carNum);//车牌号
		
		
		//商品清单
		List<ServerOrderBean> serverOrderBeans = ServerOrderBean.conver2Bean(content);
		model.put("productServer", serverOrderBeans);
		Map<String,String> map = ServerOrderBean.getSum(serverOrderBeans);
		String productSum = map.get("productSum");
		String serverSum = map.get("serverSum");
		
		if(chooseServer == 0){//没有选中一动车保服务
			serverSum = "0";
		}
		
		//优惠卷
		String youhuiSum = "0";
		//商品总价
		model.put("productSum", productSum);
		//服务总价
		model.put("serverSum", serverSum);
		
		Float sum = CommonUtil.add(productSum,serverSum);
		
		if(StringUtils.isNotEmpty(memberCouponId)){
			MemberCoupon memberCoupon = memberCouponService.find(memberCouponId);
			Coupon coupon = memberCoupon.getCoupon();
			model.addAttribute("memberCoupon", memberCoupon);
			if(coupon.getType() == 1){//满额打折
				youhuiSum = CommonUtil.subtract(sum+"", CommonUtil.mul(sum+"", coupon.getDiscount()+"")+"")+"";
			}else if(coupon.getType() == 2){//满额减免
				youhuiSum = CommonUtil.mul(coupon.getDiscount()+"", "0")+"";
			}else if(coupon.getType() == 3){
				youhuiSum = CommonUtil.mul(coupon.getDiscount()+"", "0")+"";
			}

		}else{
			model.addAttribute("memberCoupon", null);
		}
		model.put("youhuiSum", youhuiSum);
		
		String totail = CommonUtil.subtract(sum+"", youhuiSum)+"";
		//总计
		model.put("total", totail);
		
		//顶级地区
		List<Area> areas = areaService.getRootArea();
		model.addAttribute("areas", areas);
		
		//可以选择的时间
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		//System.out.println(cal.get(Calendar.HOUR_OF_DAY));
		//System.out.println(cal.get(Calendar.DAY_OF_MONTH));
		if(cal.get(Calendar.HOUR_OF_DAY) >= 16){
			cal.add(Calendar.DAY_OF_MONTH, 2);
		}else{
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		model.put("bespeakDate", DateTimeHelper.formatDateTimetoString(cal.getTime(), "yyyy-MM-dd"));
		model.put("dateTimeMap", CommonAttributes.getInstance().getDateTimeMap());
		return base + "/server_addOrder";
	}
	
	/**
	 * 将订单加入缓存
	 * @param content 商品 服务内容
	 * @param chooseServer 是否选择服务 1选择 0没选中
	 * @param memberCouponId 优惠卷
	 * @param carId 车辆ID
	 * @param userCarId 用户车管家ID
	 * @param carColor 车辆颜色
	 * @param carNum 车牌号
	 * @param areaSelectId 地区ID
	 * @param address 详细地址
	 * @param bespeakDate 预约时间 yyyy-MM-dd
	 * @param username 用户名
	 * @param phone 电话
	 * @param dateTimeSelect 时间段
	 * @return
	 */
	@RequestMapping("/existServerOrder")
	public String existServerOrder(String content,
			Integer chooseServer,String memberCouponId,
			String carId,String userCarId,String carColor,
			String carNum,
			String areaId,String address,
			String bespeakDate,String dateTimeSelect,
			String contact,String phone,
			Integer payWay){
		
		try {
			Order order = new Order();
			order.setAddress(address);
			order.setContact(contact);
			order.setPhone(phone);
			order.setPayWay(payWay);
			if(StringUtils.isNotEmpty(areaId)){
				order.setArea(new Area(areaId));
			}
			
			Car car = carService.find(carId);
			order.setCar(car);
			order.setCarColor(carColor);
			order.setCarNum(carNum);
			
			//设置优惠卷
			if(StringUtils.isNotEmpty(memberCouponId)){
				MemberCoupon memberCoupon = memberCouponService.find(memberCouponId);
				order.setMemberCoupon(memberCoupon);
			}
		
			//设置用户
			MemberBean bean = getMemberBean(request);
			if(bean != null){
				order.setMember(bean.getMember());
			}
			
			
			if(chooseServer == 1){//已选择保养服务   保养订单
				order.setType(2);
			}else{//纯商品 商品订单
				order.setType(3);
			}

			//开始服务时间
			DateMapBean mapBean = CommonAttributes.getInstance().getDateTimeMap().get(dateTimeSelect);
			String startStr = bespeakDate + " " + DateTimeHelper.formatDateTimetoString(mapBean.getStartDate(), "HH:mm");
			System.out.println(startStr);
			Date srartDate = DateTimeHelper.parseToDate(startStr, "yyyy-MM-dd HH:mm");
			order.setStartDate(srartDate);
			
			//结束服务时间
			String endStr = bespeakDate + " " + DateTimeHelper.formatDateTimetoString(mapBean.getEndDate(), "HH:mm");
			System.out.println(endStr);
			Date endDate = DateTimeHelper.parseToDate(endStr, "yyyy-MM-dd HH:mm");
			order.setEndDate(endDate);
			
			//组装订单服务 以及 商品服务
			Set<ServerOrder> serverOrderSet = new HashSet<ServerOrder>();
			Set<ServerOrderDetail> allServerOrderDetailSet = new HashSet<ServerOrderDetail>();
			List<ServerOrderBean> serverOrderBeans = ServerOrderBean.conver2Bean(content);
			for(ServerOrderBean serverBean : serverOrderBeans){
				ServerOrder serverOrder = new ServerOrder();
				serverOrder.setName(serverBean.getServerName());
				serverOrder.setPrice(Float.parseFloat(serverBean.getServerPrice()));
				serverOrder.setServiceType(new ServiceType(serverBean.getServerId()));
				
				Set<ServerOrderDetail> serverOrderDetailSet = new HashSet<ServerOrderDetail>();
				for(ServerOrderProductBean productBean : serverBean.getServerOrderProductBeans()){
					ServerOrderDetail serverOrderDetail = new ServerOrderDetail();
					serverOrderDetail.setName(productBean.getName());
					serverOrderDetail.setCount(Float.parseFloat(productBean.getCount()));
					serverOrderDetail.setPrice(Float.parseFloat(productBean.getPrice()));
					serverOrderDetail.setProduct(new Product(productBean.getId()));
					serverOrderDetail.setServerOrder(serverOrder);
					serverOrderDetailSet.add(serverOrderDetail);
				}
				serverOrder.setServerOrderDetail(serverOrderDetailSet);
				allServerOrderDetailSet.addAll(serverOrderDetailSet);
				serverOrderSet.add(serverOrder);
			}
			order.setServerOrderDetail(allServerOrderDetailSet);
			order.setServerOrder(serverOrderSet);
			
			
			Map<String,String> map = ServerOrderBean.getSum(serverOrderBeans);
			String productSum = map.get("productSum");
			String serverSum = map.get("serverSum");
			
			if(chooseServer == 0){//没有选中一动车保服务
				serverSum = "0";
			}
			
			//优惠卷
			String youhuiSum = "0";
			
			Float sum = CommonUtil.add(productSum,serverSum);
			
			if(StringUtils.isNotEmpty(memberCouponId)){
				MemberCoupon memberCoupon = memberCouponService.find(memberCouponId);
				Coupon coupon = memberCoupon.getCoupon();
				if(coupon.getType() == 1){//满额打折
					youhuiSum = CommonUtil.subtract(sum+"", CommonUtil.mul(sum+"", coupon.getDiscount()+"")+"")+"";
				}else if(coupon.getType() == 2){//满额减免
					youhuiSum = CommonUtil.mul(coupon.getDiscount()+"", "0")+"";
				}else if(coupon.getType() == 3){
					youhuiSum = CommonUtil.mul(coupon.getDiscount()+"", "0")+"";
				}
			}
			
			Float totail = CommonUtil.subtract(sum+"", youhuiSum);
		//amount_paid 优惠价     amount_money 原价    price 实际价格
			order.setAmount_money(sum);
			order.setAmount_paid(Float.parseFloat(youhuiSum));
			order.setPrice(totail);
			
			order.setNum(CommonUtil.getOrderNum());
			
			String orderCachedId = order.getNum();//IDGenerator.getRandomString(32, 0);
			CachedManager.putObjectCached("order", orderCachedId, order);
			return "redirect:gotoConfirmServer?confimId="+orderCachedId;
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
	@RequestMapping("/gotoConfirmServer")
	public String gotoConfirmServer(String confimId,ModelMap model){
		
		try {
			Order order = (Order) CachedManager.getObjectCached("order", confimId);
			
			if(order == null){
				return base + "/server_confirmOrder";
			}
			model.addAttribute("order", order);
			model.addAttribute("confimId", confimId);
			
			Car car =carService.find(order.getCar().getId()) ;
			CarBrand carBrand = car.getCarBrand();
			CarType carType = car.getCarType();
			if(order.getMemberCoupon()!=null){
				MemberCoupon memberCoupon = memberCouponService.find(order.getMemberCoupon().getId());
				model.addAttribute("memberCoupon", memberCoupon);
			}
			
			model.addAttribute("car", car);
			model.addAttribute("carBrand", carBrand);
			model.addAttribute("carType", carType);
			//原价amount_money - 产品的价格 = 
			
			String productSum = "0";
			Set<ServerOrderDetail> serverOrderDetailSet = order.getServerOrderDetail();
			for(ServerOrderDetail bean : serverOrderDetailSet){
				//String sum = bean.getSum();----
				//productSum = CommonUtil.add(productSum,sum)+"";
			}
			model.addAttribute("serviceMoney", CommonUtil.subtract(order.getAmount_money()+"", productSum));
			
			
			if(order.getArea() != null){
				model.addAttribute("area", areaService.find(order.getArea().getId()));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return base + "/server_confirmOrder";
	}
	
	@RequestMapping("/findOrderNum")
	@ResponseBody
	public String findOrderNum(String confimId){
		
		JSONArray array = new JSONArray();
		Order order = orderService.getOrderByOrderNum(confimId);
		if(order != null){
			return ajaxSuccess("成功", response);
		}
		
		return ajaxError("失败", response);
	}
	/**
	 * 跳转到确认订单页面
	 * @param confimId
	 * @return
	 */
	@RequestMapping("/saveServiceOrder")
	@ResponseBody
	public String saveServiceOrder(String confimId){
		
		try {
			Order order = (Order) CachedManager.getObjectCached("order", confimId);
			//Set<ServerOrderDetail> serverOrderDetailSet = order.getServerOrderDetail();
			Set<ServerOrder> serverOrderSet = order.getServerOrder();
			
			//order.setNum(CommonUtil.getOrderNum());
			if(order.getType() == 2){//保养服务
				order.setStatus(11);
			}else if(order.getType() == 3){//商品订单
				order.setStatus(21);
			}
			
			order.setServerOrderDetail(null);
			order.setServerOrder(null);
			orderService.save(order);
			
			for(ServerOrder bean : serverOrderSet){
				if(order.getType() == 1){//有服务
					bean.setOrder(order);
					serverOrderService.save(bean);
				}
				for(ServerOrderDetail detailbean : bean.getServerOrderDetail()){
					detailbean.setOrder(order);
					if(order.getType() == 1){//有服务
						detailbean.setServerOrder(bean);
					}else{
						detailbean.setServerOrder(null);
					}
					detailService.save(detailbean);
				}
			}
			CachedManager.remove("order", confimId);
			return ajaxSuccess("成功", response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ajaxError("", response);
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
		UserCar userCar = userCarService.find(memberBean.getDefaultCar().getId());
		//将 cleanCarAddOrder 方法返回的参数统一
		//Car car = userCar.getCar();
		UserCarBean bean = new UserCarBean(userCar.getCarNum(),userCar.getCarColor(),userCar.getCar(),userCar);
		/*bean.setCar(car);
		CarBrand carBrand = carbr
		car.getCarBrand().getImgPath();
		bean.setCarBrand(car.getCarBrand());
		bean.setCarType(car.getCarType());*/
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
		UserCarBean bean = new UserCarBean(null,null,car,null);
		model.addAttribute("cleanCar", bean);
		
		ServiceType serviceType = serviceTypeService.find(CommonAttributes.getInstance().getCleanCar());
		model.addAttribute("serviceType", serviceType);
		
		MemberBean memberBean = getMemberBean(request);
	
		model.addAttribute("member", memberBean);
		
	//	Member member = memberService.find(memberBean.getId());
		
		//优惠卷
		//List<MemberCoupon> coupons = memberCouponService.getCouponByMember(member.getId());
		
		//model.addAttribute("coupons", coupons);
		
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
			
			order.setStatus(11);
			
			Float money = order.getAmount_money();
			Float paid = order.getAmount_paid();
			Float price = CommonUtil.subtract(money+"", paid+"");
			order.setPrice(price);
			order.setType(1);
			
			order.setNum(CommonUtil.getOrderNum());
			
			Set<ServerOrder> serverOrderSet = new HashSet<ServerOrder>();
			ServerOrder serverOrder = new ServerOrder();
			
			ServiceType serviceType = serviceTypeService.find(CommonAttributes.getInstance().getCleanCar());
			
			serverOrder.setServiceType(serviceType);
			serverOrder.setName(serviceType.getName());
			serverOrder.setPrice(serviceType.getPrice());
			
			serverOrderSet.add(serverOrder);
			//将Order放入缓存
			order.setServerOrder(serverOrderSet);
			
			String orderCachedId = order.getNum();//IDGenerator.getRandomString(32, 0);
			
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
			model.addAttribute("freeTech", technicianService.isFree());//
			if(order == null){
				return base + "/cleanCar_confirmOrder";
			}
			model.addAttribute("order", order);
			model.addAttribute("confimId", confimId);
			
			Car car =carService.find(order.getCar().getId()) ;
			CarBrand carBrand = car.getCarBrand();
			CarType carType = car.getCarType();
			if(order.getMemberCoupon()!=null){
				MemberCoupon memberCoupon = memberCouponService.find(order.getMemberCoupon().getId());
				model.addAttribute("memberCoupon", memberCoupon);
			}
			
			model.addAttribute("car", car);
			model.addAttribute("carBrand", carBrand);
			model.addAttribute("carType", carType);
			
		//	model.addAttribute("freeTech", "1111111111");//technicianService.isFree()
			
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
	@RequestMapping("/saveCleanCarOrder")
	@ResponseBody
	public String save(String confimId){
		
		try {
			Order order = (Order) CachedManager.getObjectCached("order", confimId);
			//orderService.save(order);
			Set<ServerOrder> serverOrderSet = order.getServerOrder();
			
			order.setStatus(11);
			order.setServerOrderDetail(null);
			order.setServerOrder(null);
			orderService.save(order);
			
			for(ServerOrder bean : serverOrderSet){
				if(order.getType() == 1){//有服务
					bean.setOrder(order);
					serverOrderService.save(bean);
				}
				for(ServerOrderDetail detailbean : bean.getServerOrderDetail()){
					detailbean.setOrder(order);
					if(order.getType() == 1){//有服务
						detailbean.setServerOrder(bean);
					}else{
						detailbean.setServerOrder(null);
					}
					detailService.save(detailbean);
				}
			}
			
			CachedManager.remove("order", confimId);
			
		/*	ServerOrder serverOrder = new ServerOrder();
			ServiceType serverType = serviceTypeService.find(CommonAttributes.getInstance().getCleanCar());
			serverOrder.setServiceType(serverType);
			serverOrder.setPrice(serverType.getPrice());
			serverOrder.setOrder(order);
			serverOrder.setName(serverType.getName());*/
			//serverOrderService.save(serverOrder);
			
			
		//	CachedManager.remove("order", confimId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ajaxSuccess("成功", response);
	}
	
	/**
	 * 修改订单
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
	
	/**
	 * 获取空闲技师
	 * @param confimId
	 * @return
	 */
	@RequestMapping("/getFreeTechnicain")
	@ResponseBody
	public String getFreeTechnicain(String confimId){
		try {
			Order order = (Order) CachedManager.getObjectCached("order", confimId);
			if(technicianService.isFree()){
				return ajaxSuccess("成功", response);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ajaxError("", response);
	}
	
	/**
	 * 获取用户车辆列表
	 * @return
	 */
	@RequestMapping("/loadUserCarList")
	public String loadUserCarList(ModelMap model){
		MemberBean memberBean = getMemberBean(request);
		
		Member member = memberService.find(memberBean.getId());
		List<UserCar> userCar = member.getUserCarSet();
		
		model.addAttribute("userCars", userCar);
		model.addAttribute("defaultCarId", memberBean.getDefaultCar().getId());
		
		return base + "/cleanCar/cleanCar_addOrder_loadUserCarList";
	}
	
	/**
	 * 保存用户车辆
	 * @param model
	 * @param carId
	 * @param carColor
	 * @param carNum
	 * @return
	 */
	@RequestMapping("/saveUserCar")
	@ResponseBody
	public String addUserCar(ModelMap model,String carId,String carColor,String carNum){
		
		MemberBean memberBean = getMemberBean(request);
		Member member = memberService.find(memberBean.getId());
		Car car = carService.find(carId);
		
		UserCar userCar = new UserCar();
		userCar.setCar(car);
		userCar.setMember(member);
		userCar.setCarColor(carColor);
		userCar.setCarNum(carNum);
		userCarService.save(userCar);
		return ajaxSuccess("成功", response);
	}
	
	/**
	 * 保存用户车辆
	 * @param model
	 * @param carId
	 * @param carColor
	 * @param carNum
	 * @return
	 */
	@RequestMapping("/getUserCarView")
	@ResponseBody
	public String addUserCar(String userCarId){
		JSONObject obj = new JSONObject();
		
		UserCar userCar =  userCarService.find(userCarId);
		CarBrand carBrand = userCar.getCar().getCarBrand();
		CarType carType = userCar.getCar().getCarType();
		
		obj.put("brandName", carBrand.getName());
		obj.put("brandImg", carBrand.getImgPath());
		obj.put("typeName", carType.getName());
		obj.put("carName", userCar.getCar().getName());
		obj.put("carColor",userCar.getCarColor()==null?"":userCar.getCarColor());
		obj.put("carId", userCar.getCar().getId());
		obj.put("carNum", userCar.getCarNum()==null?"":userCar.getCarNum());
		
		return ajaxSuccess(obj, response);
	}
	
	/**
	 * 根据ID 获取 子地区
	 * @param request
	 * @return
	 */
	@RequestMapping("/findArea")
	@ResponseBody
	public String findArea(HttpServletRequest request){
		
		String id = request.getParameter("id");
		
		Area entity = areaService.find(id);
		Set<Area> areas = entity.getChildren();
		
		JSONArray jsonArray = new JSONArray();
		
		for(Area area : areas){
			JSONObject obj = new JSONObject();
			obj.put("id", area.getId());
			obj.put("text", area.getName());
			obj.put("hasNext", area.getChildren().size() > 0 ? "hasNext" : "");
			jsonArray.add(obj);
		}
		
		return ajaxSuccess(jsonArray, response);
	}
}
