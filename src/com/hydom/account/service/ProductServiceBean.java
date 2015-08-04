package com.hydom.account.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hydom.account.ebean.Product;
import com.hydom.util.dao.DAOSupport;

@Service
public class ProductServiceBean extends DAOSupport<Product> implements
		ProductService {

	@SuppressWarnings("unchecked")
	public List<Product> listForServer(String stid, String cid,
			int firstResult, int maxResult, String[] pids) {
		if (pids != null && pids.length > 0) {
			StringBuffer pidBuffer = new StringBuffer();
			for (String pid : pids) {
				pidBuffer.append(pid + ",");
			}
			String pidLan = pidBuffer.deleteCharAt(pidBuffer.length() - 1)
					.toString();
			String sql = "select p from Product p join p.carSet c where p.visible=?1 and p.productCategory.serviceType.id=?2 and c.id=?3 and p.id not in("
					+ pidLan + ") order by p.createDate desc";
			List<Product> list = em.createQuery(sql).setParameter(1, true)
					.setParameter(2, stid).setParameter(3, cid)
					.setFirstResult(firstResult).setMaxResults(maxResult)
					.getResultList();
			return list;
		} else {
			String sql = "select p from Product p join p.carSet c where p.visible=?1 and p.productCategory.serviceType.id=?2 and c.id=?3 order by p.createDate desc";
			List<Product> list = em.createQuery(sql).setParameter(1, true)
					.setParameter(2, stid).setParameter(3, cid)
					.setFirstResult(firstResult).setMaxResults(maxResult)
					.getResultList();
			return list;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public long countForServer(String stid, String cid, String[] pids) {
		if (pids != null && pids.length > 0) {
			StringBuffer pidBuffer = new StringBuffer();
			for (String pid : pids) {
				pidBuffer.append(pid + ",");
			}
			String pidLan = pidBuffer.deleteCharAt(pidBuffer.length() - 1)
					.toString();
			String sql = "select count(p.id) from Product p join p.carSet c where p.visible=?1 and p.productCategory.serviceType.id=?2 and c.id=?3 and p.id not in("
					+ pidLan + ")";
			return (Long) em.createQuery(sql).setParameter(1, true)
					.setParameter(2, stid).setParameter(3, cid)
					.getSingleResult();
		} else {
			String sql = "select p from Product p join p.carSet c where p.visible=?1 and p.productCategory.serviceType.id=?2 and c.id=?3";
			return (Long) em.createQuery(sql).setParameter(1, true)
					.setParameter(2, stid).setParameter(3, cid)
					.getSingleResult();
		}
	}

	@Override
	public Product defaultForServer(String stid, String cid) {
		String sql = "select p from Product p join p.carSet c where p.visible=?1 and p.productCategory.serviceType.id=?2 and c.id=?3 order by p.createDate desc";
		try {
			return (Product) em.createQuery(sql).setParameter(1, true)
					.setParameter(2, stid).setParameter(3, cid)
					.setMaxResults(1).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}
