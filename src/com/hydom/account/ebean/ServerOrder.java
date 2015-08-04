/*
 * 
 * 
 * 
 */
package com.hydom.account.ebean;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hydom.util.dao.BaseEntity;

/**
 * Entity - 订单 服务 
 * 
 */
@Entity
@Table(name = "t_order_server")
public class ServerOrder extends BaseEntity {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -7057361994167657841L;
	
	/**
	 * 服务分类
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="service_type_id")
	private ServiceType serviceType;
	
	/**
	 * 服务名称
	 */
	private String name;
	
	/**
	 * 订单
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="order_id")
	private Order order;

	/**
	 * 商品订单详情
	 */
	@OneToMany(fetch=FetchType.LAZY,mappedBy="serverOrder")
	private Set<ServerOrderDetail> serverOrderDetail = new HashSet<ServerOrderDetail>();
	
	
	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Set<ServerOrderDetail> getServerOrderDetail() {
		return serverOrderDetail;
	}

	public void setServerOrderDetail(Set<ServerOrderDetail> serverOrderDetail) {
		this.serverOrderDetail = serverOrderDetail;
	}
	
	
}