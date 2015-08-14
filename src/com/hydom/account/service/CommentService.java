package com.hydom.account.service;

import com.hydom.account.ebean.Comment;
import com.hydom.account.ebean.Product;
import com.hydom.util.dao.DAO;
import com.hydom.util.dao.PageView;

public interface CommentService extends DAO<Comment> {

	/**
	 * 根据商品分页获取该评论
	 * 
	 * @param product
	 *            商品
	 * @param hasImg
	 *            是否含有图片
	 * @param pageView
	 * @return
	 */
	PageView<Comment> getListByProduct(Product product, Integer hasImg,
			PageView<Comment> pageView);

	/**
	 * 统计某个商品的评论数
	 * 
	 * @param pid：商品ID
	 * @return
	 */
	public long countByPid(String pid);

}
