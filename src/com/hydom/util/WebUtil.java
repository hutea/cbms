package com.hydom.util;

import javax.servlet.http.HttpServletRequest;

import com.hydom.util.bean.AdminBean;

public class WebUtil {

	/**
	 * 获取当前登录后台帐户相关数据
	 * 
	 * @param request
	 * @return
	 */
	public static AdminBean getlogonAccount(HttpServletRequest request) {
		return (AdminBean) request.getSession().getAttribute(
				AdminBean.ADMIN_SESSION);
	}

}
