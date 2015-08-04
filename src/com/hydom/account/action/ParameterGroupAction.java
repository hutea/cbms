package com.hydom.account.action;

import java.util.ArrayList;
import java.util.List;

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

import com.hydom.account.ebean.Attribute;
import com.hydom.account.ebean.Parameter;
import com.hydom.account.ebean.ParameterGroup;
import com.hydom.account.service.AttributeService;
import com.hydom.account.service.ParameterGroupService;
import com.hydom.account.service.ParameterService;
import com.hydom.account.service.ProductCategoryService;
import com.hydom.util.BaseAction;
import com.hydom.util.dao.PageView;

@RequestMapping("/manage/parameterGroup")
@Controller
public class ParameterGroupAction extends BaseAction{
	
	private final static String basePath = "/account";
	private final static int mark = 1;
	
	@Resource
	private ProductCategoryService productCategoryService;
	
	@Resource
	private AttributeService attributeService;
	
	@Resource
	private ParameterService parameterService;
	
	@Resource
	private ParameterGroupService parameterGroupService;
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;

	@RequestMapping("/list")
	public String list(@RequestParam(required = false, defaultValue = "1") int page,ModelMap model) {
		
		PageView<ParameterGroup> pageView = new PageView<ParameterGroup>(null,page);
		
		StringBuffer jpql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		String queryContent = request.getParameter("queryContent");
		if(StringUtils.isNotEmpty(queryContent)){
			jpql.append("o.name like ?"+(params.size()+1));
			params.add("%"+queryContent+"%");
		}
		model.addAttribute("queryContent", queryContent);
		
		pageView.setJpql(jpql.toString());
		pageView.setParams(params.toArray());
		
		pageView = parameterGroupService.getPage(pageView);
	//	ModelAndView mav = new ModelAndView(basePath+"/service_type_list");
		model.addAttribute("pageView", pageView);
		model.addAttribute("m", mark);
		//mav.addAllObjects(model);
		return basePath+"/parameter_group_list";
	}
	
	@RequestMapping("/add")
	public String add(ModelMap model){
		model.addAttribute("m", mark);
		//商品分类
		model.addAttribute("categorys", productCategoryService.findProductCategory(null));
		
		return  basePath+"/parameter_group_add";
	}
	
	@RequestMapping("/edit")
	public String edit(ModelMap model,String id){
		
		Attribute entity = attributeService.find(id);
		model.addAttribute("entity", entity);
		
		model.addAttribute("m", mark);
		
		return  basePath+"/attribute_edit";
	}
	
	@RequestMapping("/save")
	public String save(ParameterGroup entity,String content){
		
		/*if(StringUtils.isNotEmpty(entity.getId())){
			ParameterGroup pg = parameterGroupService.find(entity.getId());
		}*/
		parameterGroupService.save(entity);
	
		if(StringUtils.isNotEmpty(content)){
			JSONArray jsonArray = JSONArray.fromObject(content);
			for(int i = 0; i < jsonArray.size(); i++){
				Parameter parameter = new Parameter();
				JSONObject obj = jsonArray.getJSONObject(i);
				
				String name = obj.getString("text");
				parameter.setName(name);
				Integer order = null;
				try{
					order = obj.getInt("order");
				}catch(Exception e){
					
				}
				parameter.setOrder(order);
				parameter.setParameterGroup(entity);
				
				String id = obj.getString("id");
				if(StringUtils.isNotEmpty(id)){
					parameter.setId(id);
					parameterService.update(parameter);
				}else{
					parameterService.save(parameter);
				}
			}
		}
		
		return  "redirect:list";
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public String delete(String[] ids){
		for(String id : ids){
			Attribute entity = attributeService.find(id);
			attributeService.remove(entity);
		}
		return ajaxSuccess("成功", response);
	}
}
