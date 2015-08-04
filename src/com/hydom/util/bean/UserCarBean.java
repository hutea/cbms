package com.hydom.util.bean;

import java.io.Serializable;

import com.hydom.core.server.ebean.Car;

/**
 * 用户车辆
 * @author Administrator
 *
 */
public class UserCarBean implements Serializable{

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 6684420136385100356L;
	
	/**
	 * 车牌
	 */
	private String carNum;
	
	/**
	 * 车牌颜色
	 */
	private String carColor;
	
	private Car car;
	
	public UserCarBean(){}
	
	public UserCarBean(String carNum,String carColor,Car car){
		this.carNum = carNum;
		this.carColor = carColor;
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

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}
	
}
