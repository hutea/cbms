package com.hydom.core.web.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hydom.account.ebean.Member;
import com.hydom.account.service.MemberService;
import com.hydom.util.BaseAction;
import com.hydom.util.bean.MemberBean;
@Controller
@RequestMapping("/user/information")
public class BasicInformationAction extends BaseAction{
@Resource
private MemberService memberService;
@Autowired
private HttpServletRequest request;
@Autowired
private HttpServletResponse response;
@RequestMapping("/info")
public ModelAndView basicinfo(){
	MemberBean bean = getMemberBean(request);
	Member member = bean.getMember();
	ModelAndView mav = new ModelAndView("/web/myInformation/basicinformation");
	mav.addObject("member", member);
	return mav;
	
}
@RequestMapping("/updatepassword")
public ModelAndView updatepassword(@RequestParam String newpassword){
	MemberBean bean = getMemberBean(request);
	Member entity = bean.getMember();
	entity.setPassword(newpassword);
	memberService.update(entity);
	ModelAndView mav = new ModelAndView("/web/myInformation/basicinformation");
	return mav;
	
}
}
