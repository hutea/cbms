package com.hydom.core.web.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.weld.bean.builtin.ee.EEResourceProducerField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.util.AlipayNotify;
import com.hydom.account.ebean.FeeRecord;
import com.hydom.account.ebean.Member;
import com.hydom.account.ebean.MemberCoupon;
import com.hydom.account.ebean.Order;
import com.hydom.account.service.FeeRecordService;
import com.hydom.account.service.MemberCouponService;
import com.hydom.account.service.MemberService;
import com.hydom.account.service.OrderService;
import com.hydom.account.service.ServerOrderDetailService;
import com.hydom.account.service.ServerOrderService;
import com.hydom.util.BaseAction;
import com.hydom.util.CommonUtil;
import com.hydom.util.PushUtil;

/**
 * web首页
 * 
 * @author liudun
 * 
 */

@RequestMapping("/web/pay")
@Controller
public class PayAction extends BaseAction {

	private static final String base = "/index";

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	@Resource
	private MemberService memberService;
	@Resource
	private OrderService orderService;
	@Resource
	private MemberCouponService memberCouponService;
	@Resource
	private ServerOrderService serverOrderService;
	@Resource
	private ServerOrderDetailService detailService;
	
	@Resource
	private FeeRecordService feeRecordService;
	
	private Log log = LogFactory.getLog("payLog");
	/**
	 * 
	 */
	@RequestMapping("/payOrder")
	@ResponseBody
	public String payOrder(String confimId) {
		//生成订单中获取order
		Order order = orderService.getOrderByOrderNum(confimId);
		
		//将该订单加入到payOrder中
		//CachedManager.putObjectCached("payOrder", order.getNum(), order);
		if(order != null){
			JSONObject obj = new JSONObject();
			obj.put("orderNum", order.getNum());
			obj.put("orderName", "一动车保服务");
			obj.put("orderPrice", order.getPrice());
			return ajaxSuccess(obj, response);
		}
		
		return ajaxError("该订单已过期，请重新添加", response);
	}

	/**
	 * 支付宝
	 * 
	 * @param confimId
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/{type}")
	public void alipayReturn(HttpServletRequest request,
			HttpServletResponse response,@PathVariable String type) throws IOException {
		try {
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"),
				// "gbk");
				params.put(name, valueStr);
			}
			
			//订单号
			String out_trade_no = new String(request.getParameter(
					"out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

			// 支付宝交易号

			String trade_no = new String(request.getParameter("trade_no")
					.getBytes("ISO-8859-1"), "UTF-8");

			// 交易状态
			String trade_status = new String(request.getParameter(
					"trade_status").getBytes("ISO-8859-1"), "UTF-8");
			
			String total_fee = new String(request.getParameter(
					"total_fee").getBytes("ISO-8859-1"), "UTF-8");
			
			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

			if (AlipayNotify.verify(params)) {// 验证成功
				// ////////////////////////////////////////////////////////////////////////////////////////
				// 请在这里加上商户的业务逻辑程序代码

				// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

				if (trade_status.equals("TRADE_FINISHED")) {
					// 判断该笔订单是否在商户网站中已经做过处理
					// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					// 如果有做过处理，不执行商户的业务程序

					// 注意：
					// 该种交易状态只在两种情况下出现
					// 1、开通了普通即时到账，买家付款成功后。
					// 2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
				} else if (trade_status.equals("TRADE_SUCCESS")) {
					// 判断该笔订单是否在商户网站中已经做过处理
					// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					// 如果有做过处理，不执行商户的业务程序

					// 注意：
					// 该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
				}

				// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
				
				if("alipay_return".equals(type)){//订单消费
					
					String m = saveServiceOrder(out_trade_no,trade_no,total_fee,2);
					response.getWriter().println(m);
					
				}else if("alipay_recharge_return".equals(type)){//充值
					String m = saveRecharge(out_trade_no,trade_no,total_fee,2);
					response.getWriter().println(m);
				}else{
					response.getWriter().println("fail");
				}
				// 请不要修改或删除
				// ////////////////////////////////////////////////////////////////////////////////////////
			} else {// 验证失败
				response.getWriter().println("fail");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().println("fail");
	}
	
	/**
	 * 订单保存
	 * @param confimId
	 * @param tradeNum
	 * @param total_fee
	 * @return
	 */
	public String saveServiceOrder(String confimId,String tradeNum,String total_fee,Integer type){
		log.info("消费编号："+tradeNum + "进入保存流程");
		try {
			//查询该订单
			Order order = orderService.getOrderByOrderNum(confimId);
			
			//BigDecimal fb = new BigDecimal(0).setScale(2, BigDecimal.ROUND_DOWN);
			
			if(!CommonUtil.compareToFloat(total_fee, order.getPrice()+"")){
				return "fail";
			}
			
			if(order.getMemberCoupon() != null){
				MemberCoupon memberCoupon = order.getMemberCoupon();
				memberCoupon.setStatus(1);
				memberCouponService.update(memberCoupon);
			}
			
			//保存一条消费记录
			FeeRecord feeRecord = new FeeRecord();
			feeRecord.setType(2);
			feeRecord.setOrder(order);
			feeRecord.setPayWay(order.getPayWay());
			
			feeRecord.setPhone(order.getPhone());
			feeRecord.setTradeNo(tradeNum);
			feeRecord.setFee(order.getPrice());
			feeRecordService.save(feeRecord);
			
			order.setIsPay(true);
			orderService.update(order);
			
			if(order.getType() == 1){//洗车订单
				PushUtil.pushTechnician(order.getId());
			}
			log.info("消费编号："+feeRecord.getTradeNo() + "保存成功");
			//删除payOrder缓存
		//	CachedManager.remove("payOrder", confimId);
			//删除order缓存
			//CachedManager.remove("order", confimId);
			return "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "fail";
	}
	
	/**
	 * 支付宝充值
	 */
	public String saveRecharge(String confimId,String tradeNum,String total_fee,Integer type){
		try{
			
			FeeRecord feeRecord = feeRecordService.findByRechargeNum(confimId);
			if(feeRecord == null){
				return "fail";
			}
			
			if(!CommonUtil.compareToFloat(total_fee, feeRecord.getFee()+"")){
				return "fail";
			}
			
			feeRecord.setPayWay(type);
			feeRecord.setVisible(true);
			feeRecord.setTradeNo(tradeNum);
			feeRecordService.update(feeRecord);
			
			Member member = feeRecord.getMember();
			
			Float sum = CommonUtil.add(member.getMoney()+"",feeRecord.getFee()+"");
			member.setMoney(sum);
			memberService.update(member);
			
			return "success";
		}catch(Exception e){
			
		}
		return "fail";
	}
	
	
	
	
	
	/**
	 * 支付宝 退费
	 * 
	 * @param confimId
	 * @return
	 */
	@RequestMapping("/alipay_refund_return")
	public void alipay_refund_return(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String,String> params = new HashMap<String,String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
				params.put(name, valueStr);
			}
			
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			//批次号

			String batch_no = new String(request.getParameter("batch_no").getBytes("ISO-8859-1"),"UTF-8");

			//批量退款数据中转账成功的笔数

			String success_num = new String(request.getParameter("success_num").getBytes("ISO-8859-1"),"UTF-8");

			//批量退款数据中的详细信息
			String result_details = new String(request.getParameter("result_details").getBytes("ISO-8859-1"),"UTF-8");

			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
			System.out.println(batch_no);
			if(AlipayNotify.verify(params)){//验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				//请在这里加上商户的业务逻辑程序代码

				//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
				
				//判断是否在商户网站中已经做过了这次通知返回的处理
					//如果没有做过处理，那么执行商户的业务程序
					//如果有做过处理，那么不执行商户的业务程序
				
				String[] detail = result_details.split("\\^");
				String tradeNo = detail[0];
				String price = detail[1];
				String status = detail[2];
				refundFeeRecord(tradeNo,price,status);
				response.getWriter().println("success");	//请不要修改或删除

				//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

				//////////////////////////////////////////////////////////////////////////////////////////
			}else{//验证失败
				response.getWriter().println("fail");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void refundFeeRecord(String tradeNo,String price,String status){
		if("SUCCESS".equals(status)){
			FeeRecord feeRecord = feeRecordService.findByHql("select o from com.hydom.account.ebean.FeeRecord o where o.tradeNo = '"+tradeNo+"'");
			Order order = feeRecord.getOrder();
			order.setStatus(34);
			order.setModifyDate(new Date());
			orderService.update(order);
			feeRecord.setIsRefund(1);
			feeRecordService.update(feeRecord);
		}
	}
	
	public static void main(String[] args) {
		String mm = "111111^dddd^sss";
		System.out.println(mm.split("\\^").toString());
	}
}
