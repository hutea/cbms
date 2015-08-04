package com.hydom.core.server.action;

import java.util.Date;
import java.util.LinkedHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hydom.core.server.ebean.CarBrand;
import com.hydom.core.server.ebean.CarType;
import com.hydom.core.server.service.CarBrandService;
import com.hydom.core.server.service.CarTypeService;
import com.hydom.util.BaseAction;
import com.hydom.util.CommonUtil;
import com.hydom.util.dao.PageView;
import com.hydom.util.dao.QueryResult;

/**
 * @Description:车系控制层
 * @author WY
 * @date 2015年7月1日 上午10:05:05
 */

@RequestMapping("/manage/carType")
@Controller
public class CarTypeAction extends BaseAction{
	
	@Resource
	private CarTypeService carTypeService;
	@Resource
	private CarBrandService carBrandService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	private int maxresult = 10;
	
	/**
	 * 添加
	 */
	@RequestMapping("/add")
	public ModelAndView addUI() {
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		String jpql = "o.visible = 1 and o.level=1";
		Object[] params = new Object[]{};
		QueryResult<CarType> carTypes = carTypeService.getScrollData(-1, -1, jpql, params, orderby);
		
		QueryResult<CarBrand> carBrands = carBrandService.getScrollData();
		ModelAndView mav = new ModelAndView("/carType/carType_add");
		mav.addObject("parents", carTypes.getResultList());
		mav.addObject("carBrands", carBrands.getResultList());
		mav.addObject("m", 2);
		return mav;
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public ModelAndView save(String name, String qp, String jp, String parentId, String carBrandId) {
		CarType carType = new CarType();
		carType.setName(name);
		carType.setQp(qp);
		carType.setJp(jp);
		CarType parent = null;
		if(parentId!=null) parent = carTypeService.find(parentId);
		carType.setParent(parent);
		carType.setCarBrand(carBrandService.find(carBrandId));
		if(parent==null){
			carType.setLevel(1);
		}else{
			carType.setLevel(parent.getLevel()+1);
		}
		carTypeService.save(carType);
		ModelAndView mav = new ModelAndView("redirect:list");
		mav.addObject("m", 2);
		return mav;
	}
	
	/**
	 * 编辑
	 */
	@RequestMapping("/edit")
	public ModelAndView editUI(@RequestParam String id) {
		CarType carType = carTypeService.find(id);
		
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		String jpql = "o.visible = 1 and o.level=1";
		Object[] params = new Object[]{};
		QueryResult<CarType> carTypes = carTypeService.getScrollData(-1, -1, jpql, params, orderby);
		
		QueryResult<CarBrand> carBrands = carBrandService.getScrollData();
		
		ModelAndView mav = new ModelAndView("/carType/carType_edit");
		mav.addObject("carType", carType);
		mav.addObject("parents", carTypes.getResultList());
		mav.addObject("carBrands", carBrands.getResultList());
		mav.addObject("m", 2);
		return mav;
	}
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(required = false, defaultValue = "1") int page, String queryContent) {
		PageView<CarType> pageView = new PageView<CarType>(maxresult, page);
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		String jpql = "o.visible = 1";
		Object[] params = new Object[]{};
		if(queryContent!=null){
			jpql+=" and (o.name like ?1 or o.parent.name like ?2 or o.carBrand.name like ?3 or o.jp like ?4 or o.qp like ?5)";
			params = new Object[]{"%"+queryContent+"%","%"+queryContent+"%","%"+queryContent+"%","%"+queryContent+"%","%"+queryContent+"%"};
		}
		pageView.setQueryResult(carTypeService.getScrollData(pageView.getFirstResult(), maxresult, jpql, params, orderby));
		request.setAttribute("pageView", pageView);
		ModelAndView mav = new ModelAndView("/carType/carType_list");
		mav.addObject("paveView", pageView);
		mav.addObject("queryContent", queryContent);
		mav.addObject("m", 2);
		return mav;
	}
	
	/**
	 * 更新
	 */
	@RequestMapping("/update")
	public ModelAndView edit(String id, String name, String qp, String jp, String parentId, String carBrandId) {
		CarType entity = carTypeService.find(id);
		entity.setName(name);
		entity.setQp(qp);
		entity.setJp(jp);
		CarType parent = null;
		if(parentId==null){
			entity.setLevel(1);
		}else{
			parent = carTypeService.find(parentId);
			entity.setLevel(parent.getLevel()+1);
		}
		entity.setParent(parent);
		entity.setCarBrand(carBrandService.find(carBrandId));
		entity.setModifyDate(new Date());
		carTypeService.update(entity);
		ModelAndView mav = new ModelAndView("redirect:list");
		return mav;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public @ResponseBody
	String delete(@RequestParam String[] ids) {
		for(String id : ids){
			CarType entity = carTypeService.find(id);
			entity.setVisible(false);
			carTypeService.update(entity);
		}
		return ajaxSuccess("成功", response);
	}
	
	/**
	 * 验证
	 */
	@RequestMapping("/check")
	public @ResponseBody
	boolean check(@RequestParam String name) {
		return carTypeService.isExist(name);
	}
	
	/**
	 * 返回字符串的全拼
	 */
	@RequestMapping("/getPinYin")
	public @ResponseBody
	String getPinYin(@RequestParam String name) {
		if(null == name) return null;
		String initial = CommonUtil.getStringPinYin(name);
		if(initial == null){
			return null;
		}else{
			return initial;
		}
	}
}
