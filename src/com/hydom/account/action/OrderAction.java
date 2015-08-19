package com.hydom.account.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hydom.account.ebean.Area;
import com.hydom.account.ebean.Member;
import com.hydom.account.ebean.MemberCoupon;
import com.hydom.account.ebean.Order;
import com.hydom.account.ebean.ServerOrder;
import com.hydom.account.ebean.ServerOrderDetail;
import com.hydom.account.service.AreaService;
import com.hydom.account.service.MemberService;
import com.hydom.account.service.OrderService;
import com.hydom.account.service.ServiceTypeService;
import com.hydom.core.server.ebean.CarTeam;
import com.hydom.core.server.ebean.Coupon;
import com.hydom.core.server.service.CarTeamService;
import com.hydom.util.BaseAction;
import com.hydom.util.CommonUtil;
import com.hydom.util.DateTimeHelper;
import com.hydom.util.dao.PageView;

@RequestMapping("/manage/order")
@Controller
public class OrderAction extends BaseAction{
	
	private final static String basePath = "/account";
	private final static int mark = 5;
	
	@Resource
	private AreaService areaService;
	@Resource
	private OrderService orderService;
	@Resource
	private ServiceTypeService serviceTypeService;
	@Resource
	private CarTeamService carTeamService;
	@Resource
	private MemberService memberService;
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;

	@RequestMapping("/list")
	public String list(@RequestParam(required = false, defaultValue = "1") int page,
			ModelMap model,String searchProp,String endOrder,String queryContent) {
		
		PageView<Order> pageView = new PageView<Order>(null,page);
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		
		StringBuffer jpql = new StringBuffer();
		List<String> params = new ArrayList<String>();
		jpql.append("o.visible = true and o.isPay = true");
		if("true".equals(endOrder)){
			jpql.append(" and o.status = 0");
		}else{
			jpql.append(" and o.status < 30");
			jpql.append(" and o.status > 0");
		}
		
		if(StringUtils.isNotEmpty(queryContent)){
			jpql.append(" and (o.num like '%"+queryContent+"%' or o.id like '%"+queryContent+"%')");
		}
		
		pageView.setJpql(jpql.toString());
		pageView.setOrderby(orderby);
		pageView = orderService.getPage(pageView);
		
	//	ModelAndView mav = new ModelAndView(basePath+"/service_type_list");
		model.addAttribute("pageView", pageView);
		model.addAttribute("m", mark);
		model.addAttribute("searchProp", searchProp);
		model.addAttribute("endOrder", endOrder);
		model.addAttribute("queryContent", queryContent);
		model.addAttribute("areas", areaService.getRootArea());
		//mav.addAllObjects(model);
		return basePath+"/order_list";
	}
	
	@RequestMapping("/detail")
	public String detail(String id,ModelMap model) {
		Order order = orderService.find(id);
		
		model.addAttribute("entity", order);
		
		
		String productSum = "0";
		String serverSum = "0";
		
		for(ServerOrder serverOrder:order.getServerOrder()){
			serverSum = CommonUtil.add(serverOrder.getPrice()+"",serverSum)+"";
		}
		
		for(ServerOrderDetail serverOrderDetail : order.getServerOrderDetail()){
			productSum = CommonUtil.add(serverOrderDetail.getSum()+"",productSum)+"";
		}
		
		Float sum = CommonUtil.add(productSum,serverSum);
		
		String youhuiSum = "0";
		if(order.getMemberCoupon() != null){
			MemberCoupon memberCoupon = order.getMemberCoupon();
			youhuiSum = orderService.getCouponPrice(memberCoupon, sum);
		}
		model.addAttribute("productSum", productSum);
		model.addAttribute("serverSum", serverSum);
		model.addAttribute("youhuiSum", youhuiSum);
		//mav.addAllObjects(model);
		return basePath+"/order_detail";
	}
	
	
	
	//获取空闲的服务车队
	@RequestMapping("/getCarTeam")
	public String getCarTeam(String areaId,@RequestParam(required=false,defaultValue="1") Integer page,ModelMap model){
		
		Area area = null;
		if(StringUtils.isNotEmpty(areaId)){
			area = areaService.find(areaId);
		}
		
		PageView<CarTeam> pageView = carTeamService.getPage(area, page);
		
		model.addAttribute("pageView", pageView);
		
		return basePath + "/order/order_car_team";
	}
	
	//将服务车队与订单绑定
	@RequestMapping("/updateOrderToTeam")
	@ResponseBody
	public String updateOrderToTeam(String teamId,String orderId){
		try{
			CarTeam carTeam = carTeamService.find(teamId);
			Order order = orderService.find(orderId);
			if(order.getType() == 2){//保养服务
				order.setStatus(12);
			}else if(order.getType() == 3){//商品
				order.setStatus(22);
			}
			//order.setStatus(2);
			order.setCarTeam(carTeam);
			orderService.update(order);
			return ajaxSuccess("成功", response);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ajaxError("失败", response);
	}
	
	//将服务车队与订单绑定
	@RequestMapping("/orderstatus")
	@ResponseBody
	public String endDate(String orderId){
		try{
			Order order = orderService.find(orderId);
			
			if(order.getStatus() == 22){
				order.setStatus(23);
			}
			orderService.update(order);
			return ajaxSuccess("成功", response);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ajaxError("失败", response);
	}
	
	//退费订单
	@RequestMapping("/errorOrder")
	@ResponseBody
	public String errorDate(String orderId){
		try{
			Order order = orderService.find(orderId);
			order.setStatus(31);
			order.setModifyDate(new Date());
			orderService.update(order);
			return ajaxSuccess("成功", response);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ajaxError("失败", response);
	}
		
	//完结订单
	@RequestMapping("/endOrder")
	@ResponseBody
	public String endOrder(String orderId){
		try{
			Order order = orderService.find(orderId);
			order.setStatus(0);
			orderService.update(order);
			return ajaxSuccess("成功", response);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ajaxError("失败", response);
	}
	
	//删除订单
	@RequestMapping("/delete")
	@ResponseBody
	public String delete(String[] ids){
		for(String id : ids){
			Area area = areaService.find(id);
			area.setVisible(false);
			areaService.update(area);
		}
		return ajaxSuccess("成功", response);
	}
	
	@RequestMapping("/serviceTest")
	@ResponseBody
	public String test(){
		JSONArray array = new JSONArray();
		/*String areaId = "ca93cd1d-4bde-46aa-ab7c-063fcf84f4bf";
		String serviceTypeId = "326b007c-b940-4fa7-be68-9be35157977f";
		Area area = areaService.find("ca93cd1d-4bde-46aa-ab7c-063fcf84f4bf");
		ServiceType serviceType = serviceTypeService.find("326b007c-b940-4fa7-be68-9be35157977f");
		
		if(serviceType.getServiceTime() == null){
			return ajaxError("错误", response);
		}
		
		List<DateMapBean> beans = orderService.getDateMapBean(areaId, new Date(), serviceTypeId);
		
		JSONArray array = new JSONArray();
		for(DateMapBean bean : beans){
			if(bean.getCarTeamCount() > 0){//车队服务数量大于0时  返回该时间段
				JSONObject obj = new JSONObject();
				obj.put("date", bean.getMapDate());
				obj.put("count", bean.getCarTeamCount());
				array.add(obj);
			}
		}*/
		
		try{
			//Date startDate = DateTimeHelper.parseToDate("2015-7-15 9:00", "yyyy-MM-dd HH:mm");
			//Date endDate = DateTimeHelper.parseToDate("2015-7-15 10:00", "yyyy-MM-dd HH:mm");
			//Area area = areaService.find("eaff89d2-988b-4ae3-8dfa-44b6bf227d21");
			//orderService.checkDateTime(startDate, endDate, area);
			Member member = memberService.find("00a3c615-65ec-43ea-92f4-8516d1e92ee7");
			System.out.println(member);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ajaxSuccess(array, response);
	}
	
	/**
	 * 申请取消订单 待审核 市场部
	 * @return
	 */
	@RequestMapping("/market_list")
	public String market_list(@RequestParam(required = false, defaultValue = "1") int page,ModelMap model,String searchProp,String queryContent) {
		
		PageView<Order> pageView = new PageView<Order>(null,page);
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("modifyDate", "desc");
		
		StringBuffer jpql = new StringBuffer();
		jpql.append("o.visible = true and o.status = 31 and o.isPay = true");
		
		if(StringUtils.isNotEmpty(queryContent)){
			jpql.append(" and (o.num like '%"+queryContent+"%' or o.id like '%"+queryContent+"%')");
		}
		
		pageView.setJpql(jpql.toString());
		pageView.setOrderby(orderby);
		pageView = orderService.getPage(pageView);
		
	//	ModelAndView mav = new ModelAndView(basePath+"/service_type_list");
		model.addAttribute("pageView", pageView);
		model.addAttribute("m", mark);
		model.addAttribute("searchProp", searchProp);
		model.addAttribute("queryContent", queryContent);
		//mav.addAllObjects(model);
		return basePath+"/order_market_cancel_list";
	}
	
	/**
	 * 市场部同意   ---》审核中
	 */
	@RequestMapping("/marketAgree")
	@ResponseBody
	public String marketAgree(String orderId){
		try{
			Order order = orderService.find(orderId);
			order.setStatus(32);
			order.setModifyDate(new Date());
			orderService.update(order);
			return ajaxSuccess("成功", response);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ajaxError("失败", response);
	}
	
	/**
	 * 审核中的列表 需财政部同意的列表
	 * @return
	 */
	@RequestMapping("/finance_list")
	public String finance_list(@RequestParam(required = false, defaultValue = "1") int page,ModelMap model,String searchProp,String queryContent){
		PageView<Order> pageView = new PageView<Order>(null,page);
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("modifyDate", "desc");
		
		StringBuffer jpql = new StringBuffer();
		jpql.append("o.visible = true and o.status = 32 and o.isPay = true");
		if(StringUtils.isNotEmpty(queryContent)){
			jpql.append(" and (o.num like '%"+queryContent+"%' or o.id like '%"+queryContent+"%')");
		}
		pageView.setJpql(jpql.toString());
		pageView.setOrderby(orderby);
		pageView = orderService.getPage(pageView);
		
	//	ModelAndView mav = new ModelAndView(basePath+"/service_type_list");
		model.addAttribute("pageView", pageView);
		model.addAttribute("m", mark);
		model.addAttribute("searchProp", searchProp);
		model.addAttribute("queryContent", queryContent);
		//mav.addAllObjects(model);
		return basePath+"/order_finance_cancel_list";
	}
	
	/**
	 * 审核中的列表 财政部同意
	 * @return
	 */
	@RequestMapping("/financeAgree")
	@ResponseBody
	public String financeAgree(String orderId, String content){
		try{
			Order order = orderService.find(orderId);
			order.setStatus(33);
			order.setModifyDate(new Date());
			orderService.update(order);
			JSONObject obj = new JSONObject();
			obj.put("trade_num", order.getFeeRecord()==null?"":order.getFeeRecord().getTradeNo());
			obj.put("payWay", order.getPayWay());
			obj.put("price", order.getPrice());
			obj.put("content", content);
			return ajaxSuccess(obj, response);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ajaxError("失败", response);
	}
	
	
	
	/**
	 * 已退款成功的订单 或者 正在退款的订单
	 * @return
	 */
	@RequestMapping("/cancel_list")
	public String cancel_list(@RequestParam(required = false, defaultValue = "1") int page,ModelMap model,String searchProp,String queryContent){
		PageView<Order> pageView = new PageView<Order>(null,page);
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("modifyDate", "desc");
		
		StringBuffer jpql = new StringBuffer();
		jpql.append("o.visible = true and o.status = 33 or o.status = 34 and o.isPay = true");
		
		if(StringUtils.isNotEmpty(queryContent)){
			jpql.append(" and (o.num like '%"+queryContent+"%' or o.id like '%"+queryContent+"%')");
		}
		
		pageView.setJpql(jpql.toString());
		pageView.setOrderby(orderby);
		pageView = orderService.getPage(pageView);
		
	//	ModelAndView mav = new ModelAndView(basePath+"/service_type_list");
		model.addAttribute("pageView", pageView);
		model.addAttribute("m", mark);
		model.addAttribute("searchProp", searchProp);
		model.addAttribute("queryContent", queryContent);
		//mav.addAllObjects(model);
		return basePath+"/order_cancel_finsh_list";
	}
	
	/**
	 * 驳回取消订单
	 * @return
	 */
	@RequestMapping("/overOrder")
	@ResponseBody
	public String overOrder(String orderId){
		try{
			Order order = orderService.find(orderId);
			order.setStatus(35);
			order.setModifyDate(new Date());
			orderService.update(order);
			return ajaxSuccess("成功", response);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ajaxError("成功", response);
	}
	
	
}
