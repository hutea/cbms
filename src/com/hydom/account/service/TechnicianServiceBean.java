package com.hydom.account.service;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Service;

import com.hydom.account.ebean.Technician;
import com.hydom.util.dao.DAOSupport;
@Service
public class TechnicianServiceBean extends DAOSupport<Technician> implements TechnicianService{

	@Override
	public boolean isExist(String account) {
		Technician technician = null;
		try {
			technician = (Technician) em
					.createQuery(
							"select o from Technician o where o.account=?1")
					.setParameter(1, account).getSingleResult();
			/*
			 * productLable = (productLable)
			 * em.createQuery("select o from ProductLabel o where o.labelName=?1"
			 * ) .setParameter(1, name).getSingleResult();
			 */
		} catch (NoResultException e) {
		}
		if (technician == null) {
			return false;
		} else {
			return true;
		}
	}
	public Technician findTechnician(String account, String password) {
		try {
			return (Technician) em.createQuery("select o from Technician o where o.account=?1 and o.password=?2")
					.setParameter(1, account).setParameter(2, password).getSingleResult();
		} catch (NoResultException e) {
			System.out.println("账户名或密码错误");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean isExistPhoneNumber(String phonenumber) {
		Technician technician = null;
		try {
			technician = (Technician) em.createQuery("select o from Technician o where o.phonenumber=?1")
					.setParameter(1, phonenumber).getSingleResult();
			/*productLable = (productLable) em.createQuery("select o from ProductLabel o where o.labelName=?1")
			.setParameter(1, name).getSingleResult();*/
		} catch (NoResultException e) {
		}
		if(technician==null){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void serverFinish() {
		// TODO Auto-generated method stub
		
	}

}
