package com.hydom.core.server.ebean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hydom.util.dao.BaseEntity;

/**
 * @Description Entity - 优惠券
 * @author WY
 * @date 2015年7月3日 上午11:59:31
 */

@Entity
@Table(name = "t_coupon")
public class Coupon extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 991331523049071823L;

	/**名称*/
	@Column(name="name",nullable=false)
	private String name;
	
	/**使用起始时间*/
	@Column(name="begin_date")
	private Date beginDate;
	
	/**使用结束时间*/
	@Column(name="end_date")
	private Date endDate;
	
	/**介绍*/
	@Column(name="introduction")
	private String introduction;
	
	/**是否启用*/
	@Column(name="is_enabled",nullable=false)
	private Boolean isEnabled = true;
	
	/**是否允许积分兑换*/
	@Column(name="is_exchange",nullable=false)
	private Boolean isExchange = false;
	
	/**优惠券类型 1满额打折 2满额打折减免 3免额多少 */
	@Column(name="type",nullable=false,length=1)
	private Integer type = 1;
	
	/**积分兑换数*/
	@Column(name="point")
	private Integer point;
	
	/**折扣额*/
	@Column(name="discount")
	private Double discount;
	
	/**最小商品价格*/
	@Column(name="min_price")
	private Double minPrice;
	
	/**剩余使用次数*/
	@Column(name="residue_count")
	private Integer residueCount;
	
	/**图片*/
	@Column(name="imgPath")
	private String imgPath;
	
	/** 逻辑删除标志 */
	@Column(name="visible",nullable=false)
	private Boolean visible = true;

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Boolean getIsExchange() {
		return isExchange;
	}

	public void setIsExchange(Boolean isExchange) {
		this.isExchange = isExchange;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	public Integer getResidueCount() {
		return residueCount;
	}

	public void setResidueCount(Integer residueCount) {
		this.residueCount = residueCount;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

}
