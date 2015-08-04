package com.hydom.account.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hydom.account.ebean.ProductBrand;
import com.hydom.account.ebean.ProductCategory;
import com.hydom.account.ebean.ServiceType;
import com.hydom.account.service.ProductBrandService;
import com.hydom.account.service.ProductCategoryService;
import com.hydom.account.service.ServiceTypeService;
import com.hydom.util.BaseAction;
import com.hydom.util.bean.BrandBean;
import com.hydom.util.dao.PageView;

@RequestMapping("/manage/productCategory")
@Controller
public class ProductCategoryAction extends BaseAction{
	
	private final static String basePath = "/account";
	private final static int mark = 1;
	
	@Resource
	private ProductCategoryService productCategoryService;
	@Resource
	private ProductBrandService productBrandService;
	@Resource
	private ServiceTypeService serviceTypeService;
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;

	@RequestMapping("/list")
	public String list(@RequestParam(required = false, defaultValue = "1") int page,ModelMap model,String searchProp) {
		
		PageView<ProductCategory> pageView = new PageView<ProductCategory>(null,page);
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("order", "asc");
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer jpql = new StringBuffer("o.visible = ?"+ (params.size()+1));
		params.add(true);
		
		if(StringUtils.isNotEmpty(searchProp)){
			jpql.append(" and o.parent.id = ?"+ (params.size()+1) );
			params.add(searchProp);
		}else{
			jpql.append(" and o.parent is null");
		}
		
		String queryContent = request.getParameter("queryContent");
		if(StringUtils.isNotEmpty(queryContent)){
			jpql.append(" and o.name like ?"+(params.size()+1));
			params.add("%"+queryContent+"%");
		}
		model.addAttribute("queryContent", queryContent);
		
		
		pageView.setJpql(jpql.toString());
		pageView.setParams(params.toArray());
		pageView.setOrderby(orderby);
		pageView = productCategoryService.getPage(pageView);
	//	ModelAndView mav = new ModelAndView(basePath+"/service_type_list");
		model.addAttribute("pageView", productCategoryService.findProductCategory(null));
		model.addAttribute("m", mark);
		model.addAttribute("searchProp", searchProp);
		//mav.addAllObjects(model);
		return basePath+"/product_category_list";
	}
	
	@RequestMapping("/add")
	public String add(ModelMap model){
		
		//1 查询分类
		model.addAttribute("categorys", productCategoryService.findProductCategory(null));
		//2 查询品牌
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("order", "asc");
		String jpql = "o.visible = ?1";
		Object[] params = {true};
		List<ProductBrand> brands = productBrandService.getList(jpql, params, orderby);
		model.addAttribute("brands", brands);
		
		//服务类型
		jpql = "o.type = 1 and o.visible = true ";
		List<ServiceType> serviceTypes = serviceTypeService.getList(jpql, null, orderby);
		List<ServiceType> newServiceTypes = new ArrayList<ServiceType>();
		for(ServiceType serviceType : serviceTypes){
			if(serviceType.getProductCategory().size() <= 0){
				newServiceTypes.add(serviceType);
			}
		}
		model.addAttribute("serviceTypes", newServiceTypes);
		
		
		model.addAttribute("m", mark);
		return  basePath+"/product_category_add";
	}
	
	@RequestMapping("/edit")
	public String edit(ModelMap model,String id){
		
		ProductCategory productCategory = productCategoryService.find(id);
		model.addAttribute("entity", productCategory);
		
		
		model.addAttribute("categorys", productCategoryService.findProductCategory(null));
		//2 查询品牌
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("order", "asc");
		String jpql = "o.visible = ?1";
		Object[] params = {true};
		List<ProductBrand> brands = productBrandService.getList(jpql, params, orderby);
		model.addAttribute("m", mark);
		
		List<ProductBrand> productBrandSet = productCategory.getProductBrandSet();
		List<BrandBean> beans = new ArrayList<BrandBean>();
		for(ProductBrand brand : brands){
			BrandBean bean = new BrandBean();
			bean.setBrand(brand);
			if(productBrandSet.contains(brand)){
				bean.setIsSelected(true);
			}else{
				bean.setIsSelected(false);
			}
			beans.add(bean);
		}
		model.addAttribute("beans", beans);
		
		//该分类下的所有子分(页面上过滤掉所有的该分类的子分类)
		model.addAttribute("children", productCategoryService.findProductCategory(productCategory));
		
		return  basePath+"/product_category_edit";
	}
	
	@RequestMapping("/save")
	public String save(ModelMap model,ProductCategory entity,String[] brands){
		
		if(brands != null){
			List<ProductBrand> productBrandSet = new ArrayList<ProductBrand>();
			for(String id : brands){
				productBrandSet.add(productBrandService.find(id));
			}
			entity.setProductBrandSet(productBrandSet);
		}
		
		if(StringUtils.isNotEmpty(entity.getId())){
			
			ProductCategory productEntity = productCategoryService.find(entity.getId());
			productEntity.setProductBrandSet(entity.getProductBrandSet());
			productEntity.setName(entity.getName());
			productEntity.setOrder(entity.getOrder());
			
			if(StringUtils.isEmpty(entity.getParent().getId())){//说明是顶级分类
				//entity.setTreePath(null);
				productEntity.setParent(null);
				//entity.setGrade(0);
			}else{
				ProductCategory parent = productCategoryService.find(entity.getParent().getId());
			//	entity.setTreePath(entity.getParent().getTreePath()+","+entity.getId());
				productEntity.setParent(parent);
			//	entity.setGrade(parent.getGrade() + 1);
			}
			productCategoryService.update(productEntity);
		}else{
			if(StringUtils.isEmpty(entity.getParent().getId())){
				//entity.setTreePath(ProductCategory.TREE_PATH_SEPARATOR);
				entity.setParent(null);
				//entity.setGrade(0);
			}else{
				ProductCategory parent = productCategoryService.find(entity.getParent().getId());
			//	entity.setTreePath(entity.getParent().getTreePath() + entity.getParent().getId() + ProductCategory.TREE_PATH_SEPARATOR);
				entity.setParent(parent);
				//entity.setGrade(parent.getGrade() + 1);
			}
			productCategoryService.save(entity);
			
			/*if(entity.getParent() != null){
				entity.setTreePath(entity.getParent().getTreePath() + entity.getParent().getId() + ProductCategory.TREE_PATH_SEPARATOR);
			}else{
				entity.setTreePath(ProductCategory.TREE_PATH_SEPARATOR);
			}
			productCategoryService.update(entity);*/
		}
		return  "redirect:list";
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public String delete(String[] ids){
		for(String id : ids){
			ProductCategory productCategory = productCategoryService.find(id);
			productCategory.setVisible(false);
			productCategoryService.update(productCategory);
		}
		return ajaxSuccess("成功", response);
	}
	
	/**
	 * 根据ID 获取 子地区
	 * @param request
	 * @return
	 */
	@RequestMapping("/findCategory")
	@ResponseBody
	public String findArea(HttpServletRequest request){
		
		String id = request.getParameter("id");
		
		ProductCategory productCategory = productCategoryService.find(id);
		Set<ProductCategory> productCategorys = productCategory.getChildren();
		
		JSONArray jsonArray = new JSONArray();
		
		for(ProductCategory child : productCategorys){
			JSONObject obj = new JSONObject();
			obj.put("id", child.getId());
			obj.put("text", child.getName());
			obj.put("hasNext", child.getChildren().size() > 0 ? "hasNext" : "");
			jsonArray.add(obj);
		}
		
		return ajaxSuccess(jsonArray, response);
	}
}
