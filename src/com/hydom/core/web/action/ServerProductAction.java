package com.hydom.core.web.action;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.hydom.account.ebean.Area;
import com.hydom.account.ebean.MemberCoupon;
import com.hydom.account.ebean.Order;
import com.hydom.account.ebean.Product;
import com.hydom.account.ebean.ProductCategory;
import com.hydom.account.ebean.ServiceType;
import com.hydom.account.service.AreaService;
import com.hydom.account.service.MemberCouponService;
import com.hydom.account.service.MemberService;
import com.hydom.account.service.OrderService;
import com.hydom.account.service.ProductService;
import com.hydom.account.service.ServiceTypeService;
import com.hydom.core.server.ebean.Car;
import com.hydom.core.server.service.CarService;
import com.hydom.user.ebean.UserCar;
import com.hydom.util.BaseAction;
import com.hydom.util.CommonAttributes;
import com.hydom.util.CommonUtil;
import com.hydom.util.IDGenerator;
import com.hydom.util.bean.MemberBean;
import com.hydom.util.bean.UserCarBean;
import com.hydom.util.cache.CachedManager;

/**
 * web 2级页面处理
 * @author liudun
 *
 */

@RequestMapping("/web/serviceProduct")
@Controller
public class ServerProductAction extends BaseAction{
	
	private static final String base = "/index/product";
	
	@Resource
	private MemberService memberService;
	@Resource
	private ServiceTypeService serviceTypeService;
	@Resource
	private AreaService areaService;
	@Resource
	private OrderService orderService;
	@Resource
	private CarService carService;
	@Resource
	private MemberCouponService memberCouponService;
	@Resource
	private ProductService productService;
	
	
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	/**
	 * 根据服务类型ID 获取 该服务类型下所有的商品
	 * @param serviceId
	 * @return
	 */
	@RequestMapping("/findProduct")
	@ResponseBody
	public String findProduct(String serviceId,String carId){
		JSONObject obj = new JSONObject();
		ServiceType serviceType = serviceTypeService.find(serviceId);
		List<ProductCategory> productCategorySet = serviceType.getProductCategory();
		ProductCategory productCategory = null;
		if(productCategorySet.size() > 0){
			productCategory = productCategorySet.get(0);
			obj.put("productCategoryId", productCategory.getId());
			obj.put("productCategoryName", productCategory.getName());
			
			Product product = productService.defaultForServer(serviceId, carId);
			
			JSONObject productObj = new JSONObject();
			if(product!=null){
				productObj.put("id", product.getId());
				productObj.put("name", product.getName());
				productObj.put("price", product.getMarketPrice());
				obj.put("product", productObj);
			}else{
				productObj.put("id", "");
				productObj.put("name", "暂无推荐商品");
				productObj.put("price", "0");
				obj.put("product", productObj);
			}
		}else{
			obj.put("productCategoryId", "");
		}
		
		return ajaxSuccess(obj, response);
	}
}
