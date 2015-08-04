package com.hydom.core.order.ebean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hydom.account.ebean.Order;
import com.hydom.account.ebean.Technician;
import com.hydom.util.dao.BaseEntity;

/**
 * 技师绑定订单和拒单记录，同时作为推送数据源
 * 
 * @author Administrator
 * 
 */
@Entity
@Table(name = "t_technician_bindrecord")
public class TechnicianBindRecord extends BaseEntity {

	private static final long serialVersionUID = 8817742254793177568L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "technician_id")
	private Technician technician;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	/** 接受=true 拒单=false */
	private Boolean accept = true;
	/** 拒单时间 */
	private Date refuseDate;
	/** 拒单距离 */
	private Double refuseDistance;
	/** 拒单原因 */
	private String refuseCause;

	public Technician getTechnician() {
		return technician;
	}

	public void setTechnician(Technician technician) {
		this.technician = technician;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Boolean getAccept() {
		return accept;
	}

	public void setAccept(Boolean accept) {
		this.accept = accept;
	}

	public Date getRefuseDate() {
		return refuseDate;
	}

	public void setRefuseDate(Date refuseDate) {
		this.refuseDate = refuseDate;
	}

	public Double getRefuseDistance() {
		return refuseDistance;
	}

	public void setRefuseDistance(Double refuseDistance) {
		this.refuseDistance = refuseDistance;
	}

	public String getRefuseCause() {
		return refuseCause;
	}

	public void setRefuseCause(String refuseCause) {
		this.refuseCause = refuseCause;
	}

}
