package com.hydom.util.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.hydom.account.ebean.Area;
import com.hydom.account.ebean.Member;
import com.hydom.core.server.ebean.Car;
import com.hydom.user.ebean.UserCar;

/**
 * 前台用户登录bean
 * @author Administrator
 *
 */
public class MemberBean implements Serializable{
	
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 6628521429317784429L;
	
	public static final String MEMBER_SESSION = "member_session";
	
	private String username;
	private String phone;
	private String address;
	private Area area;
	private Member member;
	private Set<UserCar> userCarSet = new HashSet<UserCar>();
	private UserCar defaultCar;
	
	public static MemberBean convert2MemberBean(Member member){
		if(member != null){
			MemberBean bean = new MemberBean();
			bean.address = member.getAddress();
			bean.username = member.getName();
			bean.phone = member.getMobile();
			bean.area = member.getArea();
			bean.member = member;
			bean.userCarSet = member.getUserCarSet();
			for(UserCar userCar : member.getUserCarSet()){
				if(userCar.getDefaultCar()){
					bean.defaultCar = userCar;
				}
			}
			return bean;
		}
		return null;
	}

	public UserCar getDefaultCar() {
		return defaultCar;
	}

	public void setDefaultCar(UserCar defaultCar) {
		this.defaultCar = defaultCar;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Set<UserCar> getUserCarSet() {
		return userCarSet;
	}

	public void setUserCarSet(Set<UserCar> userCarSet) {
		this.userCarSet = userCarSet;
	}
	
}
