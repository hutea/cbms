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

import com.hydom.core.server.ebean.Car;
import com.hydom.core.server.ebean.CarBrand;
import com.hydom.core.server.ebean.CarType;
import com.hydom.core.server.service.CarBrandService;
import com.hydom.core.server.service.CarService;
import com.hydom.core.server.service.CarTypeService;
import com.hydom.util.BaseAction;
import com.hydom.util.dao.PageView;
import com.hydom.util.dao.QueryResult;

/**
 * @Description 车型控制层
 * @author WY
 * @date 2015年7月1日 下午5:49:04
 */

@RequestMapping("/manage/car")
@Controller
public class CarAction extends BaseAction{
	
	@Resource
	private CarService carService;
	@Resource
	private CarBrandService carBrandService;
	@Resource
	private CarTypeService carTypeService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	private int maxresult = 10;
	
	/**
	 * 添加
	 */
	@RequestMapping("/add")
	public ModelAndView addUI(String carBrandId, String name) {
		if(carBrandId==null) carBrandId="1";
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		String jpql = "o.visible = 1 and o.carBrand = ?1 and o.level = 2";
		Object[] params = new Object[]{carBrandService.find(carBrandId)};
		QueryResult<CarType> carTypes = carTypeService.getScrollData(-1, -1, jpql, params, orderby);
		
		QueryResult<CarBrand> carBrands = carBrandService.getScrollData();
		ModelAndView mav = new ModelAndView("/car/car_add");
		mav.addObject("carTypes", carTypes.getResultList());
		mav.addObject("carBrands", carBrands.getResultList());
		mav.addObject("carBrandId", carBrandId);
		mav.addObject("name", name);
		mav.addObject("m", 2);
		return mav;
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public ModelAndView save(Car car,String name, String carTypeId, String carBrandId) {
		
		car.setName(name);
		car.setCarType(carTypeService.find(carTypeId));
		car.setCarBrand(carBrandService.find(carBrandId));
		car.setModifyDate(new Date());
		carService.save(car);
		ModelAndView mav = new ModelAndView("redirect:list");
		mav.addObject("m", 2);
		return mav;
	}
		
	/**
	 * 编辑
	 */
	@RequestMapping("/edit")
	public ModelAndView editUI(@RequestParam String id, String carBrandId, String name) {
		Car car = carService.find(id);
		
		if(name!=null) car.setName(name);
		
		if(carBrandId==null) carBrandId=car.getCarBrand().getId();
		
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		String jpql = "o.visible = 1 and o.carBrand = ?1 and o.level = 2";
		Object[] params = new Object[]{carBrandService.find(carBrandId)};
		QueryResult<CarType> carTypes = carTypeService.getScrollData(-1, -1, jpql, params, orderby);
		
		QueryResult<CarBrand> carBrands = carBrandService.getScrollData();
		
		ModelAndView mav = new ModelAndView("/car/car_edit");
		mav.addObject("car", car);
		mav.addObject("carTypes", carTypes.getResultList());
		mav.addObject("carBrands", carBrands.getResultList());
		mav.addObject("carBrandId", carBrandId);
		mav.addObject("m", 2);
		return mav;
	}
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(required = false, defaultValue = "1") int page, String queryContent) {
		PageView<Car> pageView = new PageView<Car>(maxresult, page);
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		String jpql = "o.visible = 1";
		Object[] params = new Object[]{};
		if(queryContent!=null){
			jpql+=" and (o.name like ?1 or o.carType.name like ?2 or o.carBrand.name like ?3)";
			params = new Object[]{"%"+queryContent+"%","%"+queryContent+"%","%"+queryContent+"%"};
		}
		pageView.setQueryResult(carService.getScrollData(pageView.getFirstResult(), maxresult, jpql, params, orderby));
		request.setAttribute("pageView", pageView);
		ModelAndView mav = new ModelAndView("/car/car_list");
		mav.addObject("queryContent", queryContent);
		mav.addObject("paveView", pageView);
		mav.addObject("m", 2);
		return mav;
	}
	
	/**
	 * 更新
	 */
	@RequestMapping("/update")
	public ModelAndView edit(String id, Car car, String carTypeId, String carBrandId) {
		Car entity = carService.find(id);
		entity.setName(car.getName());
		entity.setImgPath(car.getImgPath());
		entity.setCarType(carTypeService.find(carTypeId));
		entity.setCarBrand(carBrandService.find(carBrandId));
		entity.setModifyDate(new Date());
		carService.update(entity);
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
			Car entity = carService.find(id);
			entity.setVisible(false);
			carService.update(entity);
		}
		return ajaxSuccess("成功", response);
	}
	
	/**
	 * 验证
	 */
	@RequestMapping("/check")
	public @ResponseBody
	boolean check(@RequestParam String name) {
		return carService.isExist(name);
	}
}
