package com.hydom.account.action;

import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hydom.account.ebean.Account;
import com.hydom.account.ebean.PrivilegeGroup;
import com.hydom.account.service.AccountService;
import com.hydom.account.service.PrivilegeGroupService;
import com.hydom.util.BaseAction;
import com.hydom.util.dao.PageView;

@RequestMapping("/manage/account")
@Controller
public class AccountAction extends BaseAction{
	@Resource
	private AccountService accountService;
	@Resource
	private PrivilegeGroupService groupService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	private int maxresult = 10;

	@RequestMapping("/list")
	public ModelAndView list(
			@RequestParam(required = false, defaultValue = "1") int page) {
		PageView<Account> pageView = new PageView<Account>(maxresult, page);
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		pageView.setQueryResult(accountService.getScrollData(
				pageView.getFirstResult(), maxresult, orderby));

//		StringBuffer jpql = new StringBuffer("o.visilbe=?1");
//		List<Object> params = new ArrayList<Object>();
//		params.add(true);
//		if(""){
//			jpql.append(" and o.username like?"+(params.size()+1));
//			params.add("xxxx");
//		}
//		accountService.getScrollData(pageView.getFirstResult(), page,
//				jpql.toString(), params.toArray(), orderby);
		
		request.setAttribute("pageView", pageView);
		ModelAndView mav = new ModelAndView("/account/account_list");
		mav.addObject("paveView", pageView);
		mav.addObject("m", 3);
		return mav;
	}

	@RequestMapping("/add")
	public ModelAndView addUI() {
		List<PrivilegeGroup> groups = groupService.getScrollData()
				.getResultList();
		ModelAndView mav = new ModelAndView("/account/account_add");
		mav.addObject("groups", groups);
		mav.addObject("m", 3);
		return mav;
	}

	@RequestMapping("/save")
	public ModelAndView add(@ModelAttribute Account account,
			@RequestParam(required = false) String[] gids) {
		accountService.save(account);
		ModelAndView mav = new ModelAndView("redirect:list");
		return mav;
	}

	@RequestMapping("/edit")
	public ModelAndView editUI(@RequestParam String acid) {
		Account account = accountService.find(acid);
		List<PrivilegeGroup> groups = groupService.getScrollData()
				.getResultList();
		request.setAttribute("groups", groups);
		StringBuffer ugs = new StringBuffer();
		for (PrivilegeGroup group : account.getGroups()) {
			ugs.append("#" + group.getId());
		}
		ModelAndView mav = new ModelAndView("/account/account_edit");
		mav.addObject("account", account);
		mav.addObject("ugs", ugs.toString());
		mav.addObject("m", 3);
		return mav;
	}

	@RequestMapping("/update")
	public ModelAndView edit(@ModelAttribute Account account,
			@RequestParam String[] gids) {
		Account entity = accountService.find(account.getId());
		entity.setUsername(account.getUsername());
		entity.setPassword(account.getPassword());
		entity.setNickname(account.getNickname());
		accountService.update(entity);
		ModelAndView mav = new ModelAndView("redirect:list");
		return mav;
	}

	@RequestMapping("/delete")
	public @ResponseBody
	String delete(@RequestParam String[] ids) {
		for(String id : ids){
			Account entity = accountService.find(id);
			entity.setVisible(false);
			accountService.update(entity);
		}
		return ajaxSuccess("成功", response);
	}
}
