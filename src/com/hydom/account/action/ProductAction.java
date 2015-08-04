package com.hydom.account.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
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
import org.springframework.web.multipart.MultipartFile;

import com.hydom.account.ebean.Attribute;
import com.hydom.account.ebean.Parameter;
import com.hydom.account.ebean.ParameterGroup;
import com.hydom.account.ebean.Product;
import com.hydom.account.ebean.ProductBrand;
import com.hydom.account.ebean.ProductCategory;
import com.hydom.account.ebean.ProductImage;
import com.hydom.account.ebean.ProductLabel;
import com.hydom.account.ebean.Specification;
import com.hydom.account.ebean.SpecificationValue;
import com.hydom.account.service.AttributeService;
import com.hydom.account.service.ParameterGroupService;
import com.hydom.account.service.ParameterService;
import com.hydom.account.service.ProductCategoryService;
import com.hydom.account.service.ProductLabelService;
import com.hydom.account.service.ProductService;
import com.hydom.account.service.SpecificationService;
import com.hydom.account.service.SpecificationValueService;
import com.hydom.core.server.ebean.Car;
import com.hydom.core.server.ebean.CarBrand;
import com.hydom.core.server.ebean.CarType;
import com.hydom.core.server.service.CarBrandService;
import com.hydom.core.server.service.CarService;
import com.hydom.core.server.service.CarTypeService;
import com.hydom.util.BaseAction;
import com.hydom.util.CommonUtil;
import com.hydom.util.DateTimeHelper;
import com.hydom.util.ImageUtils;
import com.hydom.util.UploadImageUtil;
import com.hydom.util.dao.PageView;

@RequestMapping("/manage/product")
@Controller
public class ProductAction extends BaseAction{
	
	private final static String basePath = "/account";
	private final static int mark = 1;
	
	@Resource
	private ProductCategoryService productCategoryService;
	@Resource
	private AttributeService attributeService;
	@Resource
	private ProductService productService;
	@Resource
	private ParameterGroupService parameterGroupService;
	@Resource
	private ParameterService parameterService;
	@Resource
	private SpecificationValueService specificationValueService;
	@Resource
	private SpecificationService specificationService;
	@Resource
	private CarBrandService carBrandService;
	@Resource
	private CarService carService;
	@Resource
	private CarTypeService carTypeService;
	@Resource
	private ProductLabelService productLabelService;
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;

	@RequestMapping("/list")
	public String list(@RequestParam(required = false, defaultValue = "1") int page,ModelMap model) {
		PageView<Product> pageView = new PageView<Product>(null, page);
		String jpql = "o.visible = ?1";
		List<Object> params = new ArrayList<Object>();
		params.add(true);
		pageView.setJpql(jpql);
		pageView.setParams(params.toArray());
		pageView = productService.getPage(pageView);
		
		model.addAttribute("pageView", pageView);
		
		model.addAttribute("m", mark);
		//mav.addAllObjects(model);
		return basePath+"/product_list";
	}
	
	
	@RequestMapping("/add")
	public String add(ModelMap model){
		model.addAttribute("m", mark);
		
		//商品分类
		model.addAttribute("productCategorys", productCategoryService.findProductCategory(null));
		//车辆品牌
		String jpql = "o.visible = ?1";
		List<Object> params = new ArrayList<Object>();
		params.add(true);
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		
		List<CarBrand> carbrands = carBrandService.getList(jpql, params.toArray(), orderby);
	//	model.addAttributes("carBrands",carbrands);
		model.addAttribute("carBrands",carbrands);
		
		//商品保修标签
		List<ProductLabel> labels = productLabelService.getProductLabelVisible(true);
		model.addAttribute("labels",labels);
		
		return  basePath+"/product_add";
	}
	
	
	@RequestMapping("/edit")
	public String edit(ModelMap model,String id){
		
		Attribute entity = attributeService.find(id);
		model.addAttribute("entity", entity);
		
		model.addAttribute("m", mark);
		
		return  basePath+"/attribute_edit";
	}
	
	/**
	 * 
	 * @param files 展示图片
	 * @param product 商品基本信息
	 * @param attributeIds 筛选参数
	 * @param attributeValues 筛选参数值
	 * @param parameterIds 商品参数ID
	 * @param parameterValues 商品参数值
	 * @param specificationValueIds 规格值
	 * @return
	 */
	@RequestMapping("/save")//ModelMap model,Attribute entity,String[] attributeValues, 
	public String save(@RequestParam MultipartFile[] files,Product product,
			@RequestParam(required=false) String[] attributeIds,
			@RequestParam(required=false) String[] attributeValues,
			@RequestParam(required=false) String[] parameterIds,
			@RequestParam(required=false) String[] parameterValues,
			@RequestParam(required=false) String[] specificationValueIds,
			@RequestParam(required=false) String[] labelIds,
			@RequestParam(required=false) String[] carBrandIds,
			@RequestParam(required=false) String[] carIds){
			
			product.setGoods_num(DateTimeHelper.getSystemTime()+"");//商品系列号
			product.setFullName(product.getName());
			product.setPrice(product.getMarketPrice());
			product.setVisible(true);
			
			ProductCategory productCategory = productCategoryService.find(product.getProductCategory().getId());
			
			Set<Specification> specifications = new HashSet<Specification>();
			specifications.addAll(productCategory.getSpecificationSet());
			
			product.setSpecifications(specifications);
			
			if(attributeIds != null){
				for (int i = 0; i < attributeIds.length; i++) {
					Attribute attribute = attributeService.find(attributeIds[i]);
					product.setAttributeValue(attribute, attributeValues[i]);
				}
			}
		
			if(parameterIds != null){
				Map<Parameter,String> map = new HashMap<Parameter, String>();
				for (int i = 0; i < parameterIds.length; i++) {
					Parameter parameter = parameterService.find(parameterIds[i]);
					map.put(parameter, parameterValues[i]);
				}
				product.setParameterValue(map);
			}
			
			//适用车辆
			if(carBrandIds != null){
				if(carBrandIds.toString().indexOf("-1") <= -1){
					if(carIds != null){
						List<Car> carList = carService.getList(carIds);
						Set<Car> carSet = new HashSet<Car>();
						carSet.addAll(carList);
						product.setCarSet(carSet);
						product.setUseAllCar(1);
					}
				}else{//适用所有车型
					product.setUseAllCar(0);
				}
			}else{//默认 适用所有车型
				product.setUseAllCar(0);
			}
			
			//商品支持的售后服务
			if(labelIds != null){
				List<ProductLabel> labels = productLabelService.getList(labelIds);
				
				Set<ProductLabel> labelSet = new HashSet<ProductLabel>();
				labelSet.addAll(labels);
				
				product.setLabels(labelSet);
			}
			
			if(files != null){
				List<ProductImage> imgs = new ArrayList<ProductImage>();
				for (MultipartFile file : files) {
					ProductImage  img = new ProductImage();
					Map<String,String> map = UploadImageUtil.uploadFile(file,request);
					img.setSource(map.get("source"));
					img.setLarge(map.get("big"));
					img.setThumbnail(map.get("small"));
					img.setOrder(imgs.size());
					imgs.add(img);
				}
				product.setProductImages(imgs);
			}
			
			//商品规格存在 则保存规格 商品规格不存在  则直接保存该商品 
			if(specificationValueIds != null){
				for (int i = 0; i < specificationValueIds.length; i++) {
					Product entity = new Product();
					entity = product;
					entity.setSn(DateTimeHelper.getSystemTime()+"");
					Set<SpecificationValue> specificationValueSet = new HashSet<SpecificationValue>();
					
					String[] specificationValues = specificationValueIds[i].split(",");
					for(String specificationValue : specificationValues){
						SpecificationValue v = specificationValueService.find(specificationValue);
						specificationValueSet.add(v);
					}
					entity.setSpecificationValues(specificationValueSet);
					
					productService.save(entity);
				}
			}else{
				product.setSn(DateTimeHelper.getSystemTime()+"");
				productService.save(product);
			}
			
			
		/*Map<String,String> map = uploadFile(bean);
		System.out.println(map);*/
	/*	List<String> options = new ArrayList<String>();
		for(String attributeValue : attributeValues){
			if(StringUtils.isNotEmpty(attributeValue)){
				options.add(attributeValue);
			}
		}
		if(StringUtils.isNotEmpty(entity.getId())){
			entity.setOptions(options);
			attributeService.update(entity);
		}else{
			entity.setPropertyIndex(null);
			entity.setOptions(options);
			attributeService.save(entity);
		}*/
		return  "redirect:list";
	}
	
	
	@RequestMapping("/delete")
	@ResponseBody
	public String delete(String[] ids){
		for(String id : ids){
			Product product = productService.find(id);
			product.setVisible(false);
			productService.update(product);
		}
		return ajaxSuccess("成功", response);
	}
	
	
	
	//获取商品品牌
	@RequestMapping("/findBrand")
	@ResponseBody
	public String findBrand(String productCategoryId){
		JSONArray array = new JSONArray();
		
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		List<ProductBrand> productBrands = productCategory.getProductBrandSet();
		for(ProductBrand entity : productBrands){
			JSONObject obj = new JSONObject();
			obj.put("id", entity.getId());
			obj.put("name", entity.getName());
			array.add(obj);
		}
		
		return ajaxSuccess(array, response);
	}
	
	//获取商品参数
	@RequestMapping("/loadParameter")
	public String getParamenterPage(ModelMap model, String productCategoryId){
		
		String jpql = "o.productCategory.id = ?1";
		List<Object> params = new ArrayList<Object>();
		params.add(productCategoryId);
		
		ParameterGroup parameterGroup = parameterGroupService.find(jpql, params.toArray());
		if(parameterGroup != null){
			List<Parameter> parameters = parameterGroup.getParameters();
			model.addAttribute("parameters", parameters);
		}
		return basePath+"/product/product_load_parameter";
	}
	
	//获取商品筛选条件
	@RequestMapping("/loadAttribute")
	public String loadAttribute(ModelMap model, String productCategoryId){
		
		String jpql = "o.productCategory.id = ?1";
		List<Object> params = new ArrayList<Object>();
		params.add(productCategoryId);
		
		List<Attribute> attributes = attributeService.getList(jpql, params.toArray(),null);
		if(attributes.size() > 0){
			model.addAttribute("attribute", attributes.get(0));
		}
		
		return basePath+"/product/product_load_attribute";
	}
	
	//获取商品规格
	@RequestMapping("/loadSpecification")
	public String loadSpecification(ModelMap model, String productCategoryId){
		
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		model.addAttribute("specifications", productCategory.getSpecificationSet());
		
		return basePath+"/product/product_load_specification";
	}
	
	
	//获取该品牌下的车系
	@RequestMapping("/getCarType")
	@ResponseBody
	public String getCarType(String carBrandId){
		JSONArray array = new JSONArray();
		CarBrand carBrand = carBrandService.find(carBrandId);
		List<CarType> carTypeSet = carBrand.getCarTypeList();
		for(CarType carType : carTypeSet){
			JSONObject obj = new JSONObject();
			obj.put("id", carType.getId());
			obj.put("text", carType.getName());
			array.add(obj);
		}
		return ajaxSuccess(array, response);
	}
	
	//获取该品牌下的车系
	@RequestMapping("/getCar")
	@ResponseBody
	public String getCar(String carTypeId){
		JSONArray array = new JSONArray();
		CarType carType = carTypeService.find(carTypeId);
		List<Car> carList = carType.getCarList();
		for(Car car : carList){
			JSONObject obj = new JSONObject();
			obj.put("id", car.getId());
			obj.put("text", car.getName());
			array.add(obj);
		}
		return ajaxSuccess(array, response);
	}
	
	@RequestMapping("/test")
	@ResponseBody
	public String test(@RequestParam(required=false) String[] carIds){
		JSONArray array = new JSONArray();
		
		List<Car> carList = carService.getList(carIds);
		for(Car car : carList){
			JSONObject obj = new JSONObject();
			obj.put("id", car.getId());
			obj.put("text", car.getName());
			array.add(obj);
		}
		return ajaxSuccess(array, response);
	}
}
