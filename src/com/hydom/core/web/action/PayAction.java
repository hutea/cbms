package com.hydom.core.web.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hydom.account.ebean.Order;
import com.hydom.account.service.FeeRecordService;
import com.hydom.account.service.MemberCouponService;
import com.hydom.account.service.MemberService;
import com.hydom.account.service.OrderService;
import com.hydom.account.service.ServerOrderDetailService;
import com.hydom.account.service.ServerOrderService;
import com.hydom.util.BaseAction;
import com.hydom.util.CommonUtil;
import com.hydom.util.payUtil.AlipayUtil;
import com.hydom.util.payUtil.WeChatPayUtil;
import com.tencent.WXPay;
import com.tencent.protocol.pay_protocol.ScanPayReqData;

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
	 * 微信支付的订单生成
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping("/wenxin/payOrder")
	@ResponseBody
	public String wenxinOrder(HttpServletRequest request, HttpServletResponse response,String order_num)
			throws Exception {
		String result = "fail";
		order_num = "20150817184133511567";
		Order order = orderService.getOrderByOrderNum(order_num);
		// 将该订单加入到payOrder中
		// CachedManager.putObjectCached("payOrder", order.getNum(), order);
		String orderNum = order.getNum();
		String orderName = "一动车保服务";
		long amount = 1l;CommonUtil.getLong(order.getPrice(), 100,0);
		
		Map<String,Object> retMap = (Map<String, Object>) new WeChatPayUtil().getParameterMap(orderNum,orderName, amount, "NATIVE", WeChatPayUtil.pay_return,getIp(request));
		JSONObject obj = new JSONObject();
		for (String key : retMap.keySet()) {
			String m = new String(retMap.get(key).toString().getBytes(), "UTF-8");
			obj.put(key, m);
		}
		Object code_url = retMap.get("code_url");
		Object prepay_id = retMap.get("prepay_id");
		
		
		
		return ajaxSuccess(code_url, response);
	}
	
	public static void main(String[] args) {
		Float price = CommonUtil.mul("1.24", "100");
		BigDecimal b = new BigDecimal(price);
		long b1 = b.setScale(0).longValue();
		System.out.println(b1);
	}
	
	
	/**
	 * 支付回调统一接口
	 * 
	 * @param confimId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/{type}")
	public void alipayReturn(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String type)
			throws IOException {
		String result = "fail";
		try {
			// 获取支付宝POST过来反馈信息
			if (type.contains("alipay")) {
				Map<String, String> params = new HashMap<String, String>();
				Map requestParams = request.getParameterMap();
				for (Iterator iter = requestParams.keySet().iterator(); iter
						.hasNext();) {
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
				result = AlipayUtil.dealwith(params, type);
			}else if(type.contains("weixin")){
				result = WeChatPayUtil.dealwith(request);
				result = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
				response.getWriter().println(result);
				return ;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().println(result);
	}

}
