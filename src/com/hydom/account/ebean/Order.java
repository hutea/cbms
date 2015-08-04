/*
 * 
 * 
 * 
 */
package com.hydom.account.ebean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hydom.core.server.ebean.Car;
import com.hydom.core.server.ebean.CarTeam;
import com.hydom.util.DateTimeHelper;
import com.hydom.util.dao.BaseEntity;

/**
 * Entity - 订单
 * 
 */
@Entity
@Table(name = "t_order")
public class Order extends BaseEntity {
	private static final long serialVersionUID = 69734876638441645L;

	/** 洗车订单 保养订单 公用字段 */

	/** 联系电话 */
	private String phone;

	/** 联系人 */
	private String contact;

	/** 详细地址 */
	private String address;

	/** 支付方式 1=货到付款；2=支付宝 ；3=银联 4=微信 **/
	private Integer payWay;

	/** 配送方式 1=上门服务； 2=到门店 **/
	private Integer serverWay = 1;

	/** 优惠价 **/
	private Float amount_paid;

	/** 原价 **/
	private Float amount_money;

	/** 实际价格 **/
	private Float price;

	/** 状态 1、审核中的订单 2、服务中的订单 3、完结订单 4、退费订单 5、失败订单 **/
	private Integer status;

	/** 类型 1洗车订单 2保养订单 **/
	private Integer type;

	/** 服务开始时间 **/
	@Column(name = "start_date")
	private Date startDate;

	/** 服务时间结束时间 **/
	@Column(name = "end_date")
	private Date endDate;

	/** 车型 **/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "car_id")
	private Car car;

	/**
	 * 车牌
	 */
	private String carNum;

	/**
	 * 车辆颜色
	 */
	private String carColor;

	/**
	 * 经度
	 */
	private Double lng;

	/**
	 * 纬度
	 */
	private Double lat;

	/** 用户 **/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;


	/** 优惠卷 **/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_coupon_id")
	private MemberCoupon memberCoupon;
	
	/**
	 * 服务订单
	 */
	@OneToMany(fetch=FetchType.LAZY,mappedBy="order")
	private Set<ServerOrder> serverOrder = new HashSet<ServerOrder>();
	
	/**
	 * 订单中的商品
	 * @return
	 */
	@OneToMany(fetch=FetchType.LAZY,mappedBy="order")
	private Set<ServerOrderDetail> serverOrderDetail = new HashSet<ServerOrderDetail>();
	
	/** 公共字段结束 
	 * 流程      订单保存     1 服务订单  
	 * 2、保存各服务订单中的所有商品
	 */
	
	/** 洗车专用字段 */

	/**
	 * 技师
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "technician_id")
	private Technician techMember;

	/** 技师与用户之间的距离：在绑定技师时填充该数据 */
	private Double distance;

	/**
	 * 清洗方式 1 内部清洗 2 内外清洗
	 */
	@Column(name = "cleantype")
	private Integer cleanType;

	/**
	 * 保养服务专用
	 */

	/**
	 * 预约时间
	 */
	@Column(name = "mark_startdate")
	private Date makeStartDate;
	
	/** 车队 **/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "car_team_id")
	private CarTeam carTeam;
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getPayWay() {
		return payWay;
	}

	public void setPayWay(Integer payWay) {
		this.payWay = payWay;
	}

	public Integer getServerWay() {
		return serverWay;
	}

	public void setServerWay(Integer serverWay) {
		this.serverWay = serverWay;
	}

	public Float getAmount_paid() {
		return amount_paid;
	}

	public void setAmount_paid(Float amount_paid) {
		this.amount_paid = amount_paid;
	}

	public Float getAmount_money() {
		return amount_money;
	}

	public void setAmount_money(Float amount_money) {
		this.amount_money = amount_money;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getCarColor() {
		return carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public MemberCoupon getMemberCoupon() {
		return memberCoupon;
	}

	public void setMemberCoupon(MemberCoupon memberCoupon) {
		this.memberCoupon = memberCoupon;
	}

	public Integer getCleanType() {
		return cleanType;
	}

	public void setCleanType(Integer cleanType) {
		this.cleanType = cleanType;
	}

	public Technician getTechMember() {
		return techMember;
	}

	public void setTechMember(Technician techMember) {
		this.techMember = techMember;
	}

	public Date getMakeStartDate() {
		return makeStartDate;
	}

	public void setMakeStartDate(Date makeStartDate) {
		this.makeStartDate = makeStartDate;
	}

	public CarTeam getCarTeam() {
		return carTeam;
	}

	public void setCarTeam(CarTeam carTeam) {
		this.carTeam = carTeam;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
	
	public Set<ServerOrder> getServerOrder() {
		return serverOrder;
	}

	public void setServerOrder(Set<ServerOrder> serverOrder) {
		this.serverOrder = serverOrder;
	}

	public Set<ServerOrderDetail> getServerOrderDetail() {
		return serverOrderDetail;
	}

	public void setServerOrderDetail(Set<ServerOrderDetail> serverOrderDetail) {
		this.serverOrderDetail = serverOrderDetail;
	}

	@Transient
	public String getDateTimeMap() {
		Date startDate = this.startDate;
		Date endDate = this.endDate;

		String time = DateTimeHelper.formatDateTimetoString(startDate,
				"yyyy-MM-dd");

		String start = DateTimeHelper
				.formatDateTimetoString(startDate, "HH:mm");
		String end = DateTimeHelper.formatDateTimetoString(endDate, "HH:mm");

		return time + "(" + start + " - " + end + ")";
	}

	@Transient
	public String getStatusString() {
		Integer stauts = this.status;
		/** 状态 1、审核中的订单 2、服务中的订单 3、完结订单 4、退费订单 **/
		if (stauts == 1) {
			return "审核中";
		} else if (stauts == 2) {
			return "服务中";
		} else if (stauts == 3) {
			return "已完结";
		} else {
			return "退费";
		}
	}
}