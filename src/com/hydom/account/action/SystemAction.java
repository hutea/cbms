package com.hydom.account.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hydom.account.ebean.SystemParam;
import com.hydom.account.service.SystemParamService;
import com.hydom.util.BaseAction;
import com.hydom.util.CommonAttributes;
import com.hydom.util.bean.SystemBean;

/**
 * 系统参数
 * @author Administrator
 *
 */
@RequestMapping("/manage/system")
@Controller
public class SystemAction extends BaseAction {
	
	private static String base = "/account";
	
	@Resource
	private SystemParamService systemParamService;
	
	
	
	@RequestMapping("/view")
	public String getSystemView(ModelMap model){
		SystemBean bean = CommonAttributes.getInstance().getSystemBean();
		model.addAttribute("entity", bean);
		return base + "/system_view";
	}
	
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param content
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public String getUpdate(@RequestParam String startDate,@RequestParam String endDate,String content,HttpServletResponse response){
		try{
			JSONObject obj = new JSONObject();
			obj.put("startDate", startDate);
			obj.put("endDate", endDate);
			obj.put("content", content);
			
			SystemParam systemParam = systemParamService.find(CommonAttributes.getInstance().getSystemId());
			systemParam.setContent(obj.toString());
			systemParamService.update(systemParam);
			
			CommonAttributes.getInstance().setSystemParam();
			
			return ajaxSuccess("", response);
		}catch(Exception e){
			
		}
		return ajaxError("修改出错", response);
	}
	
	
	
}
