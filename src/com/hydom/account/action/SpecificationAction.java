package com.hydom.account.action;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hydom.account.ebean.Specification;
import com.hydom.account.ebean.SpecificationValue;
import com.hydom.account.service.AttributeService;
import com.hydom.account.service.ProductCategoryService;
import com.hydom.account.service.ProductService;
import com.hydom.account.service.SpecificationService;
import com.hydom.account.service.SpecificationValueService;
import com.hydom.util.BaseAction;
import com.hydom.util.UploadImageUtil;
import com.hydom.util.dao.PageView;

@RequestMapping("/manage/specification")
@Controller
public class SpecificationAction extends BaseAction {

	private final static String basePath = "/account";
	private final static int mark = 1;

	@Resource
	private ProductCategoryService productCategoryService;
	@Resource
	private AttributeService attributeService;
	@Resource
	private ProductService productService;
	@Resource
	private SpecificationService specificationService;
	@Resource
	private SpecificationValueService specificationValueService;
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;

	@RequestMapping("/list")
	public String list(
			@RequestParam(required = false, defaultValue = "1") int page,
			ModelMap model) {
		
		PageView<Specification> pageView = new PageView<Specification>();
		pageView = specificationService.getPage(pageView);
		
		
		model.addAttribute("m", mark);
		model.addAttribute("pageView", pageView);
		// mav.addAllObjects(model);
		return basePath + "/specification_list";
	}

	@RequestMapping("/add")
	public String add(ModelMap model) {
		model.addAttribute("m", mark);
		model.addAttribute("productCategorys", productCategoryService.findProductCategory(null));
		return basePath + "/specification_add";
	}

	@RequestMapping("/edit")
	public String edit(ModelMap model, String id) {
		Specification specification = specificationService.find(id);
		model.addAttribute("m", mark);
		model.addAttribute("entity", specification);
		model.addAttribute("productCategorys", productCategoryService.findProductCategory(null));
		return basePath + "/specification_edit";
	}

	@RequestMapping("/save")
	public String save(ModelMap model, Specification entity,
			@RequestParam(required = false) MultipartFile[] specificationFiles,
			@RequestParam(required = false) String[] specificationNames,
			@RequestParam(required = false) Integer[] specificationOrders) {
			
			if(StringUtils.isEmpty(entity.getProductCategory().getId())){
				entity.setProductCategory(null);
			}
			
			if(StringUtils.isNotEmpty(entity.getId())){
				Specification specification = specificationService.find(entity.getId());
				specification.setProductCategory(entity.getProductCategory());
				specification.setMemo(entity.getMemo());
				specification.setName(entity.getName());
				specification.setOrder(entity.getOrder());
				specificationService.update(specification);
				entity = specification;
			}else{
				specificationService.save(entity);
			}
			if(specificationNames != null && specificationNames.length > 0){
				for(int i = 0; i<specificationNames.length; i++){
					
					SpecificationValue specificationValue = new SpecificationValue();
					
					specificationValue.setName(specificationNames[i]);
					specificationValue.setSpecification(entity);
					
					if(specificationFiles !=null && specificationFiles.length>0){
						MultipartFile file = specificationFiles[i];
						Map<String,String> imgMap = UploadImageUtil.uploadFile(file,request);
						specificationValue.setImage(imgMap.get("source"));
					}
					
					if(specificationOrders[i]!=null&&specificationOrders.length>0){
						specificationValue.setOrder(specificationOrders[i]);
					}
					
					specificationValueService.save(specificationValue);
					
				}
			}
		return "redirect:list";
	}

	@RequestMapping("/delete")
	@ResponseBody
	public String delete(String id) {
		try{
			Specification specification = specificationService.find(id);
			if (specification != null && specification.getProducts() != null && !specification.getProducts().isEmpty()) {
				return ajaxError("有商品在使用该数据,无法删除", response);
			}
			specificationService.delete(id);
			return ajaxSuccess("成功", response);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return ajaxError("失败", response);
	}
	
	@RequestMapping("/deleteValue")
	@ResponseBody
	public String deleteValue(String id) {
		try{
			specificationValueService.deleteBySql(id);
			return ajaxSuccess("成功", response);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ajaxError("失败", response);
	}
}
