package com.hydom.account.ebean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统参数表。id即为key值。
 * 
 * @author Administrator
 * 
 */
@Entity
@Table(name = "t_systemparam")
public class SystemParam {

	/**
	 * suparea=支持的服务地区
	 */
	@Id
	private String id;

	private String content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
