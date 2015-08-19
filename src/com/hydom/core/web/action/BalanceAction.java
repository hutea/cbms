package com.hydom.core.web.action;

import java.util.LinkedHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hydom.account.ebean.FeeRecord;
import com.hydom.account.ebean.Member;
import com.hydom.account.service.FeeRecordService;
import com.hydom.account.service.MemberService;
import com.hydom.util.BaseAction;
import com.hydom.util.CommonUtil;
import com.hydom.util.bean.MemberBean;
import com.hydom.util.dao.PageView;

@Controller
@RequestMapping("/user/balance")
public class BalanceAction extends BaseAction {

	@Resource
	private MemberService memberService;
	@Resource
	private FeeRecordService recordService;
	
	
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;

	@RequestMapping("/view")
	public String view(ModelMap model){
		
		MemberBean bean = getMemberBean(request);
		Member member = memberService.find(bean.getId());
		model.addAttribute("member", member);
		
		return "/web/balance/balance";
	}
	
	@RequestMapping("/pay")
	@ResponseBody
	public String pay(ModelMap model,String price,Integer type){
		try{
			FeeRecord feeRecord = new FeeRecord();
			
			feeRecord.setFee(Float.parseFloat(price));
			feeRecord.setVisible(false);
			feeRecord.setPayWay(type);
			feeRecord.setType(1);
			feeRecord.setRechargeNo(CommonUtil.getOrderNum());
			
			MemberBean memberBean = getMemberBean(request);
			if(memberBean == null){
				return ajaxError("登录已失效", response);
			}
			
			feeRecord.setMember(memberService.find(memberBean.getId()));
			
			feeRecord.setPhone(memberBean.getPhone());
			
			recordService.save(feeRecord);
			
			JSONObject obj = new JSONObject();
			obj.put("orderNum", feeRecord.getRechargeNo());
			obj.put("orderPrice", feeRecord.getFee());
			obj.put("orderName", "帐号预充值");
			
			return ajaxSuccess(obj, response);
			
		}catch(Exception e){
			
		}
		
		return ajaxError("充值失败", response);
	}
	
	@RequestMapping("/list")
	public String list(ModelMap model,@RequestParam(defaultValue="1",required=false) Integer page){
		MemberBean bean = getMemberBean(request);
		Member member = memberService.find(bean.getId());
		PageView<FeeRecord> pageView = new PageView<FeeRecord>(null,page);
		
		String sql = "o.phone = ?1 and o.visible=true";
		pageView.setJpql(sql);
		Object[] params = new Object[]{member.getMobile()};
		pageView.setParams(params);
		
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		pageView.setOrderby(orderby);
		
		pageView = recordService.getPage(pageView);
		
		model.addAttribute("pageView", pageView);
		
		return "/web/balance/balance_list";
	}
}
