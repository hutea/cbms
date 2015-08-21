package com.hydom.util.payUtil;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hydom.account.ebean.FeeRecord;
import com.hydom.account.ebean.Member;
import com.hydom.account.ebean.MemberCoupon;
import com.hydom.account.ebean.Order;
import com.hydom.account.service.FeeRecordService;
import com.hydom.account.service.MemberCouponService;
import com.hydom.account.service.MemberService;
import com.hydom.account.service.OrderService;
import com.hydom.util.CommonUtil;
import com.hydom.util.PushUtil;
import com.hydom.util.SpringUtil;

@Component
public class PayCommonUtil {
	private Log log = LogFactory.getLog("payLog");
	
	/**
	 * 订单保存
	 * @param out_trade_no 订单编号
	 * @param trade_no 商家交易号
	 * @param total_fee 交易金额
	 * @param type 交易类型
	 * @return
	 */
	public String saveServiceOrder(String confimId,String tradeNum,String total_fee,Integer type){
		OrderService orderService = (OrderService) SpringUtil.getBean("orderService");
		MemberCouponService memberCouponService = (MemberCouponService) SpringUtil.getBean("memberCouponService");
		FeeRecordService feeRecordService = (FeeRecordService) SpringUtil.getBean("feeRecordService");
		
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
			FeeRecord feeRecord = feeRecordService.findByNumAndOrderConfirmId(tradeNum,confimId);
			if(feeRecord == null){
				feeRecord = new FeeRecord();
			}
			feeRecord.setType(2);
			feeRecord.setOrder(order);
			feeRecord.setPayWay(order.getPayWay());
			
			feeRecord.setPhone(order.getPhone());
			feeRecord.setTradeNo(tradeNum);
			feeRecord.setFee(order.getPrice());
			if(StringUtils.isNotEmpty(feeRecord.getId())){
				feeRecordService.update(feeRecord);
			}else{
				feeRecordService.save(feeRecord);
			}
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
	 * 充值接口
	 * @param out_trade_no 订单编号
	 * @param trade_no 商家交易号
	 * @param total_fee 交易金额
	 * @param type 交易类型
	 * @return
	 */
	public String saveRecharge(String out_trade_no,String trade_no,String total_fee,Integer type){
		MemberService memberService = (MemberService) SpringUtil.getBean("memberService");
		FeeRecordService feeRecordService = (FeeRecordService) SpringUtil.getBean("feeRecordService");
		try{
			
			FeeRecord feeRecord = feeRecordService.findByRechargeNum(out_trade_no);
			
			if(feeRecord == null){
				return "fail";
			}
			
			if(!CommonUtil.compareToFloat(total_fee, feeRecord.getFee()+"")){
				return "fail";
			}
			
			feeRecord.setType(1);
			feeRecord.setPayWay(type);
			feeRecord.setVisible(true);
			feeRecord.setTradeNo(trade_no);
			feeRecordService.update(feeRecord);
			
			Member member = feeRecord.getMember();
			
			Float sum = CommonUtil.add(member.getMoney()+"",feeRecord.getFee()+"");
			member.setMoney(sum);
			memberService.update(member);
			
			return "success";
		}catch(Exception e){
			e.printStackTrace();
		}
		return "fail";
	}
	
	/**
	 * 退款接口
	 * @param tradeNo 商家交易号
	 * @param price 推荐金额
	 * @param status 状态
	 * @param batch_no 商家退款交易号
	 */
	public String refundFeeRecord(String tradeNo,String price,String status, String batch_no){
		
		OrderService orderService = (OrderService) SpringUtil.getBean("orderService");
		FeeRecordService feeRecordService = (FeeRecordService) SpringUtil.getBean("feeRecordService");
		
		if("SUCCESS".equals(status)){
			FeeRecord feeRecord = feeRecordService.findByHql("select o from com.hydom.account.ebean.FeeRecord o where o.tradeNo = '"+tradeNo+"'");
			Order order = feeRecord.getOrder();
			order.setStatus(34);
			order.setModifyDate(new Date());
			orderService.update(order);
			feeRecord.setIsRefund(1);
			feeRecord.setRefundNo(batch_no);
			feeRecordService.update(feeRecord);
			return "success";
		}
		return "fail";
	}
}
