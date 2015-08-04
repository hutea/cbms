/*
 * 
 * 
 * 
 */
package com.hydom.account.ebean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hydom.core.server.ebean.Coupon;
import com.hydom.util.dao.BaseEntity;

/**
 * Entity - 会员
 * 
 * 
 * 
 */
@Entity
@Table(name = "t_member_coupon")
public class MemberCoupon extends BaseEntity {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 3915996210726461009L;
	
	/**
	 * 使用时间
	 */
	@Column(name="use_date")
	private Date useDate;
	
	/**
	 * 领取时间
	 */
	@Column(name="receive_date")
	private Date receiveDate;
	
	/**
	 * 优惠券使用状态 0未使用 1已使用 2已过期
	 */
	@Column(name="status")
	private Integer status=0;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="member_id")
	private Member member;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="coupon_id")
	private Coupon coupon;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getUseDate() {
		return useDate;
	}

	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}
	
	
}