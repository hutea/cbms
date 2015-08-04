package com.hydom.account.service;

import org.springframework.stereotype.Service;

import com.hydom.account.ebean.ProductBrand;
import com.hydom.util.dao.DAOSupport;

@Service
public class ProductBrandServiceBean extends DAOSupport<ProductBrand> implements  ProductBrandService {

}
