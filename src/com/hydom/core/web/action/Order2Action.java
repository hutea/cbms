package com.hydom.core.web.action;

import java.util.LinkedHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hydom.account.ebean.Order;
import com.hydom.account.ebean.Technician;
import com.hydom.account.service.OrderService;
import com.hydom.util.BaseAction;
import com.hydom.util.dao.PageView;
@Controller
@RequestMapping("/web/order")
public class Order2Action extends BaseAction{

	@Resource
	private OrderService orderService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;

	
	private int maxresult = 10;
	
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(required = false, defaultValue = "1") int page, String memberId) {
		/*System.out.println("这是memberID"+memberId);*/
		PageView<Order> pageView = new PageView<Order>(maxresult, page);
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		String jpql = "o.member.id = ?1";
		Object[] params = new Object[]{};
		params = new Object[]{memberId};
		
		/*if(queryContent!=null){
			jpql+="o.name like ?1";
			params = new Object[]{"%"+queryContent+"%"};
		}*/
		
		pageView.setQueryResult(orderService.getScrollData(pageView.getFirstResult(), maxresult, jpql, params, orderby));
		
		request.setAttribute("pageView", pageView);
		ModelAndView mav = new ModelAndView("/web/order_list");
		mav.addObject("paveView", pageView);
		
		return mav;
	 
	}
	@RequestMapping("/details")
	public ModelAndView details(@RequestParam String id){
		Order order = orderService.find(id);
		ModelAndView mav = new ModelAndView("/web/order_details");
		mav.addObject("order", order);
		return mav;
		}
}
