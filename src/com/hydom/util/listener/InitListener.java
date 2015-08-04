/*
 * 
 * 
 * 
 */
package com.hydom.util.listener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.dom4j.DocumentException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.hydom.util.CommonAttributes;


/**
 * Listener - 初始化
 * 
 * 
 * 
 */
@Component("initListener")
public class InitListener implements ServletContextAware, ApplicationListener<ContextRefreshedEvent> {
	
	private ServletContext servletContent;
	public void setServletContext(ServletContext servletContext) {
		this.servletContent = servletContext;
	}
	
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		System.out.println("初始化系统设置......");
		try {
			initCleanServerConfig();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化洗车ID
	 * @throws DocumentException
	 */
	void initCleanServerConfig() throws DocumentException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.properties");
		Properties pro = new Properties();
		try {
			pro.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		CommonAttributes.getInstance().setCleanCar(pro.getProperty("server.cleancar"));
	}
}