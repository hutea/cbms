package com.hydom.user.service;

import org.springframework.stereotype.Service;

import com.hydom.user.ebean.UserCar;
import com.hydom.util.dao.DAOSupport;

@Service("userCarService")
public class UserCarServiceBean extends DAOSupport<UserCar> implements
		UserCarService {

	@Override
	public UserCar defaultCar(String uid) {
		try {
			return (UserCar) em
					.createQuery(
							"select o from UserCar o where o.visible=?1 and  o.member.id=?2 and o.defaultCar=?3")
					.setParameter(1, true).setParameter(2, uid)
					.setParameter(3, true).setMaxResults(1).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void resetUndefault(String uid) {
		em.createQuery(
				"update UserCar o set o.defaultCar=?1  where o.member.id=?2")
				.setParameter(1, false).setParameter(2, uid).executeUpdate();
	}

}
