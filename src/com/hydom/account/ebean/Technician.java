package com.hydom.account.ebean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hydom.util.dao.BaseEntity;

/**
 * 技师表
 * @author FXW
 *
 */
@Entity
@Table(name = "t_technician")
public class Technician extends BaseEntity{

	
	private static final long serialVersionUID = 7969987728818977496L;
	
	/**帐号*/
	@Column(name="account",nullable=false)
	private String account;
	/**密码*/
	@Column(name="password",nullable=false)
	private String password = "123456";
	/**姓名*/
	@Column(name="name",nullable=false)
	private String name;
	/**联系电话*/
	@Column(name="phonenumber",nullable=false)
	private String phonenumber;
	/**工作状态*/
	//0为空闲中，1为派单中，2为服务中
	@Column(name="stats")
	private int stats = 0;
	
	/**是否上班*/
	//false为没上班，true为上班
	@Column(name="jobstatus")
	private boolean jobstatus = false;
	
	public boolean isJobstatus() {
		return jobstatus;
	}
	public void setJobstatus(boolean jobstatus) {
		this.jobstatus = jobstatus;
	}
	/**经度*/
	@Column(name="longitude")
	private Double longitude;
	
	/**纬度*/
	@Column(name="latitude")
	private Double latitude;
	
	/**技师星级*/
	
	@Column(name="level")
	private Double level;
	
	/**头像*/
	@Column(name="ico")
	private String imgPath;
	
	private Boolean visible = true;
	
	public Double getLongitude() {
		return longitude;
	}
	public Double getLevel() {
		return level;
	}
	public void setLevel(Double level) {
		this.level = level;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public int getStats() {
		return stats;
	}
	public void setStats(int stats) {
		this.stats = stats;
	}
}
