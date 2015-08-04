package com.hydom.account.service;

import java.util.List;

import com.hydom.account.ebean.SystemPrivilege;
import com.hydom.util.dao.DAO;

public interface SystemPrivilegeService extends DAO<SystemPrivilege> {

	public void saves(List<SystemPrivilege> sps);

	public SystemPrivilege findByURL(String url);

	public List<SystemPrivilege> listBylevel(int level);

}
