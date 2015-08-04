/*
 * 
 * 
 * 
 */
package com.hydom.util;

/**
 * 公共参数
 * 
 * 
 * 
 */
public final class CommonAttributes {

	/** 日期格式配比 */
	public static final String[] DATE_PATTERNS = new String[] { "yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss" };

	/** system.xml文件路径 */
	public static final String SYSTEM_XML_PATH = "/system.xml";

	/** config.properties文件路径 */
	public static final String CONFIG_PROPERTIES_PATH = "/config.properties";

	/**
	 * 不可实例化
	 */
	private CommonAttributes() {
	}
	
	private static CommonAttributes instance;
	
	/**
	 * 洗车服务ID
	 */
	private String cleanCar;
	
	public static CommonAttributes getInstance(){
		if(instance == null){
			instance = new CommonAttributes();
		}
		return instance;
	}

	public String getCleanCar() {
		return cleanCar;
	}

	public void setCleanCar(String cleanCar) {
		this.cleanCar = cleanCar;
	}
}