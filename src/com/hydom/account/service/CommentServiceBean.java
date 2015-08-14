package com.hydom.account.service;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.hydom.account.ebean.Comment;
import com.hydom.account.ebean.Product;
import com.hydom.util.dao.DAOSupport;
import com.hydom.util.dao.PageView;

@Service
public class CommentServiceBean extends DAOSupport<Comment> implements
		CommentService {

	@Override
	public long countByPid(String pid) {
		return (Long) em
				.createQuery(
						"select count(o.id) from Comment o where o.serverOrderDetail.product.id=?1")
				.setParameter(1, pid).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageView<Comment> getListByProduct(Product product, Integer hasImg,
			PageView<Comment> page) {
		// TODO Auto-generated method stub
		String baseHql = "from Comment o join o.serverorderdetail so where so.product = :product and o.commentType = :commentType ";
		/*
		 * if(hasImg != null){ hql += "and o."; }
		 */

		String hql = "select o " + baseHql;
		hql += " order by o.createDate desc";
		Query query = em.createQuery(hql);
		query.setFirstResult(page.getFirstResult());
		query.setMaxResults(page.getMaxresult());
		query.setParameter("product", product);
		query.setParameter("commentType", 0);
		page.setRecords(query.getResultList());

		String countHql = "select count(o.id) " + baseHql;
		Query countQuery = em.createQuery(countHql);
		countQuery.setParameter("product", product);
		countQuery.setParameter("commentType", 0);
		page.setTotalrecord((Long) countQuery.getSingleResult());

		return page;
	}

}
