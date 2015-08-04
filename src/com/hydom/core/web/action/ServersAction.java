package com.hydom.core.web.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hydom.account.ebean.Server;
import com.hydom.account.service.ServerService;
import com.hydom.util.BaseAction;

/**
 *@author liuyulin
 *@date 2015年7月14日下午2:44:13
 *@file NewsAction.java
 */
@Controller
@RequestMapping("web/server")
public class ServersAction extends BaseAction {
	
	@Resource
	private ServerService serverService;

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	
	@RequestMapping("/detail")
	public String detail(String id,ModelMap model){
		Server server=serverService.find(id);
		model.put("server", server);
		return "/server/service";
	}
	
	@RequestMapping("/service")
	public String service(){
		return "/server/service";
	}
	
	@RequestMapping("/appdown")
	public String appDownlod(){
		return "/server/appDown";
	}
}
