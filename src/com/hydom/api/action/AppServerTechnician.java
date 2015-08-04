package com.hydom.api.action;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hydom.account.ebean.Technician;
import com.hydom.account.service.TechnicianService;

/**
 * @Description:移动端技师相关接口
 * @author WY
 * @date 2015年7月27日 下午3:22:16
 */

@RequestMapping("/api/technician")
@Controller
public class AppServerTechnician {

	@Resource
	private TechnicianService technicianService;
	
	private Log log = LogFactory.getLog("dataServer");
	
	/**
	 * 登录
	 */
	@RequestMapping(value = "login", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String signin(String account, String password) {
		try {
			log.info("App【技师登录】：" + "账号=" + account);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			Technician technician = technicianService.findTechnician(account, password);
			if(technician == null){
				dataMap.put("result", "701"); // 用户名或密码错误
				String json = mapper.writeValueAsString(dataMap);
				return json;
			}
			dataMap.put("result", "001");
			dataMap.put("id", technician.getId());
			dataMap.put("name", technician.getName());
			dataMap.put("phonenumber", technician.getPhonenumber());
			dataMap.put("stats", technician.getStats());
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}
}
