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

import com.hydom.account.ebean.Area;
import com.hydom.account.ebean.Order;
import com.hydom.account.ebean.ServiceType;
import com.hydom.account.service.AreaService;
import com.hydom.account.service.OrderService;
import com.hydom.account.service.ServiceTypeService;
import com.hydom.core.server.ebean.CarTeam;
import com.hydom.core.server.service.CarTeamService;
import com.hydom.util.BaseAction;
import com.hydom.util.DateTimeHelper;
import com.hydom.util.bean.DateMapBean;
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
	
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;

	@RequestMapping("/list")
	public String list(@RequestParam(required = false, defaultValue = "1") int page,ModelMap model,String searchProp) {
		
		PageView<Order> pageView = new PageView<Order>(null,page);
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		
		StringBuffer jpql = new StringBuffer();
		List<String> params = new ArrayList<String>();
		jpql.append("o.status in (1,2)");
		pageView.setJpql(jpql.toString());
		pageView = orderService.getPage(pageView);
		
	//	ModelAndView mav = new ModelAndView(basePath+"/service_type_list");
		model.addAttribute("pageView", pageView);
		model.addAttribute("m", mark);
		model.addAttribute("searchProp", searchProp);
		//mav.addAllObjects(model);
		return basePath+"/order_list";
	}
	
	//获取空闲的服务车队
	@RequestMapping("/getCarTeam")
	@ResponseBody
	public String getCarTeam(String orderId){
		JSONArray array = new JSONArray();
		/*Order order = orderService.find(orderId);
		Area area = order.getArea();
		List<CarTeam> carTeamList = area.getCarTeam();
		if(carTeamList.size() <= 0){
			return null;
		}
		
		List<Order> orders = orderService.getBindingOrder(order);
		
		for(Order o : orders){
			carTeamList.remove(o.getCarTeam());
		}
		
		JSONArray array = new JSONArray();
		if(orders == null || carTeamList.size() == 0){
			return ajaxError("暂无车队", response);
		}
		for(CarTeam ct : carTeamList){
			JSONObject obj = new JSONObject();
			obj.put("id", ct.getId());
			obj.put("name", ct.getHeadMember());
			array.add(obj);
		}*/
		
		return ajaxSuccess(array, response);
	}
	
	//将服务车队与订单绑定
	@RequestMapping("/updateOrderToTeam")
	@ResponseBody
	public String updateOrderToTeam(String teamId,String orderId){
		try{
			CarTeam carTeam = carTeamService.find(teamId);
			Order order = orderService.find(orderId);
			order.setStatus(2);
			order.setCarTeam(carTeam);
			orderService.update(order);
			return ajaxSuccess("成功", response);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ajaxError("失败", response);
	}
	
	//将服务车队与订单绑定
	@RequestMapping("/endOrder")
	@ResponseBody
	public String endDate(String orderId){
		try{
			Order order = orderService.find(orderId);
			order.setStatus(3);
			orderService.update(order);
			return ajaxSuccess("成功", response);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ajaxError("失败", response);
	}
	
	//将服务车队与订单绑定
	@RequestMapping("/errorOrder")
	@ResponseBody
	public String errorDate(String orderId){
		try{
			Order order = orderService.find(orderId);
			order.setStatus(5);
			orderService.update(order);
			return ajaxSuccess("成功", response);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ajaxError("失败", response);
	}
		
	
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
			Date startDate = DateTimeHelper.parseToDate("2015-7-15 9:00", "yyyy-MM-dd HH:mm");
			Date endDate = DateTimeHelper.parseToDate("2015-7-15 10:00", "yyyy-MM-dd HH:mm");
			Area area = areaService.find("eaff89d2-988b-4ae3-8dfa-44b6bf227d21");
			orderService.checkDateTime(startDate, endDate, area);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return ajaxSuccess(array, response);
	}
}
