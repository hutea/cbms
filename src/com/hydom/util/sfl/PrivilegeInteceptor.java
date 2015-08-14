package com.hydom.util.sfl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hydom.account.ebean.Account;
import com.hydom.account.ebean.Privilege;
import com.hydom.account.ebean.PrivilegeGroup;
import com.hydom.account.service.PrivilegeService;
import com.hydom.util.WebUtil;
import com.hydom.util.bean.AdminBean;

/**
 * 权限细粒度拦截器
 * 
 * @author www.hydom.cn [heou]
 * 
 */
@Component
public class PrivilegeInteceptor extends HandlerInterceptorAdapter {
	@Resource
	private PrivilegeService systemPrivilegeService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		try {

			Account account = WebUtil.getlogonAccount(request).getMember();
			if (account != null && "admin".equals(account.getUsername())) {
				return true;
			}
			StringBuffer requestUrl = request.getRequestURL();
			if (request.getQueryString() != null
					&& request.getQueryString().length() > 0) {
				requestUrl.append("#" + request.getQueryString());
			}
			String url = requestUrl.substring(requestUrl.indexOf("manage"));
			Privilege sp = systemPrivilegeService.findByURL(url);
			if (sp != null) { // request url is required
				if (account != null && account.getGroups() != null) {
					System.out.println(account + "-->account");
					for (PrivilegeGroup group : account.getGroups()) {
						if (group.getPrivileges().contains(sp)) {
							return true;
						}
					}
					return false;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} catch (Exception e) {
			return true; // 测试返回true,正式返回false
		}

	}

}
