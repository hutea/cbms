package com.hydom.core.server.action;

import java.util.LinkedHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hydom.account.ebean.WorkLog;
import com.hydom.account.service.WorkLogService;
import com.hydom.util.BaseAction;
import com.hydom.util.dao.PageView;

/**
 * @Description 考勤记录控制层
 * @author WY
 * @date 2015年8月15日 下午6:30:05
 */

@RequestMapping("/manage/workLog")
@Controller
public class WorkLogAction extends BaseAction{
	
	@Resource
	private WorkLogService workLogService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	private int maxresult = 10;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(required = false, defaultValue = "1") int page, String queryContent, String queryDate) {
		PageView<WorkLog> pageView = new PageView<WorkLog>(maxresult, page);
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		String jpql = "";
		Object[] params = new Object[]{};
		if(queryContent!=null && queryDate==null){
			jpql="o.technician.phonenumber like ?1 or o.technician.name like ?2";
			params = new Object[]{"%"+queryContent+"%","%"+queryContent+"%"};
		}else if(queryDate!=null && queryContent!=null){
			jpql="convert(varchar(10),o.createDate,120) = ?1 and (o.technician.phonenumber like ?2 or o.technician.name like ?3)";
			params = new Object[]{queryDate,"%"+queryContent+"%","%"+queryContent+"%"};
		}else if(queryDate!=null && queryContent==null){
//			jpql="convert(varchar(10),o.createDate,120) = ?1";
			jpql="o.createDate between ?1 and ?1";
			params = new Object[]{queryDate};
		}
		pageView.setQueryResult(workLogService.getScrollData(pageView.getFirstResult(), maxresult, jpql, params, orderby));
		request.setAttribute("pageView", pageView);
		ModelAndView mav = new ModelAndView("/workLog/workLog_list");
		mav.addObject("paveView", pageView);
		mav.addObject("queryContent", queryContent);
		mav.addObject("queryDate", queryDate);
		mav.addObject("m", 10);
		return mav;
	}
}
