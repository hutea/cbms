package com.hydom.api.action;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hydom.account.ebean.Area;
import com.hydom.account.ebean.Member;
import com.hydom.account.ebean.News;
import com.hydom.account.ebean.Order;
import com.hydom.account.ebean.Product;
import com.hydom.account.ebean.ProductBrand;
import com.hydom.account.ebean.ProductCategory;
import com.hydom.account.ebean.ServerOrder;
import com.hydom.account.ebean.ServerOrderDetail;
import com.hydom.account.ebean.ServiceType;
import com.hydom.account.service.AreaService;
import com.hydom.account.service.MemberService;
import com.hydom.account.service.NewsService;
import com.hydom.account.service.OrderService;
import com.hydom.account.service.ProductBrandService;
import com.hydom.account.service.ProductCategoryService;
import com.hydom.account.service.ProductService;
import com.hydom.account.service.ServiceTypeService;
import com.hydom.api.ebean.ShortMessage;
import com.hydom.api.ebean.Token;
import com.hydom.api.service.ShortMessageService;
import com.hydom.api.service.TokenService;
import com.hydom.core.server.ebean.Car;
import com.hydom.core.server.ebean.CarBrand;
import com.hydom.core.server.ebean.CarType;
import com.hydom.core.server.service.CarBrandService;
import com.hydom.core.server.service.CarService;
import com.hydom.core.server.service.CarTypeService;
import com.hydom.user.ebean.UserCar;
import com.hydom.user.service.UserCarService;
import com.hydom.util.IDGenerator;
import com.hydom.util.bean.DateMapBean;
import com.hydom.util.dao.PageView;

@RequestMapping("/api")
@Controller
public class AppServer {
	@Resource
	private MemberService memberService;
	@Resource
	private ShortMessageService shortMessageService;
	@Resource
	private TokenService tokenService;
	@Resource
	private ServiceTypeService serviceTypeService;
	@Resource
	private CarBrandService carBrandService;
	@Resource
	private CarTypeService carTypeService;
	@Resource
	private AreaService areaService;
	@Resource
	private OrderService orderService;
	@Resource
	private CarService carService;
	@Resource
	private UserCarService userCarService;
	@Resource
	private ProductBrandService productBrandService;
	@Resource
	private ProductCategoryService productCategoryService;
	@Resource
	private ProductService productService;
	@Resource
	private NewsService newsService;

	private Log log = LogFactory.getLog("dataServerLog");

	/**
	 * 注册登录
	 */
	@RequestMapping("/user/signin")
	public @ResponseBody
	String signin(String phone, String code) {
		try {
			log.info("App【用户登录】：" + "手机号=" + phone + " 验证码=" + code);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			// step1：验证码合法性验证
			ShortMessage shortMessage = shortMessageService.findByPhoneAndCode(
					phone, code, 1);
			if (shortMessage == null) {
				dataMap.put("result", "601"); // 验证码错误
				dataMap.put("uid", "");
				dataMap.put("token", "");
				String json = mapper.writeValueAsString(dataMap);
				return json;
			}
			if (System.currentTimeMillis()
					- shortMessage.getCreateDate().getTime() > 5 * 60 * 1000) {
				dataMap.put("result", "602"); // 验证码过期
				dataMap.put("uid", "");
				dataMap.put("token", "");
				String json = mapper.writeValueAsString(dataMap);
				return json;
			}
			// step2：进行注册或登录
			Member member = memberService.findByMobile(phone);
			if (member == null) { // 为空，则进行注册
				member = new Member();
				member.setMobile(phone);
				memberService.save(member);
			}
			// step3：产生安全令牌-->获取上次的令牌环?
			Token token = new Token();
			token.setId(IDGenerator.uuid());
			token.setAuthid(IDGenerator.uuid());
			token.setCreateDate(new Date());
			token.setUid(member.getId());
			tokenService.save(token);

			dataMap.put("result", "001");
			dataMap.put("uid", member.getId());
			dataMap.put("token", token.getAuthid());
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/***
	 * 获取服务类型
	 */
	@RequestMapping(value = "/server/category", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String category(@RequestParam(required = false) String uid,
			@RequestParam(required = false) String token) {
		try {
			log.info("App【获取服务类型】：" + "uid=" + uid + " token=" + token);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			/** 服务类型 数据获取 **/
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("type", "desc");// 使得上门洗车和上门保养排前
			orderby.put("order", "asc");
			StringBuffer jpql = new StringBuffer("o.visible=?1");
			List<Object> params = new ArrayList<Object>();
			params.add(true);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<ServiceType> types = serviceTypeService.getScrollData(
					jpql.toString(), params.toArray(), orderby).getResultList();
			for (ServiceType type : types) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("scid", type.getId());
				map.put("scname", type.getName());
				map.put("scimage", type.getImgPath());
				map.put("scremark", type.getRemark());
				map.put("scremark1", type.getRemark1());
				map.put("scremark2", type.getRemark2());
				map.put("scprice", type.getPrice());
				list.add(map);
			}
			dataMap.put("result", "001");
			dataMap.put("list", list);
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/***
	 * 获取车辆品牌信息
	 */
	@RequestMapping(value = "/server/carbrand", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String carbrand(@RequestParam(required = false) String uid,
			@RequestParam(required = false) String token) {
		try {
			log.info("App【获取车辆品牌信息】：" + "uid=" + uid + " token=" + token);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			/** 车辆品牌 数据获取 **/
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuffer jpql = new StringBuffer("o.visible=?1");
			List<Object> params = new ArrayList<Object>();
			params.add(true);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<CarBrand> brands = carBrandService.getScrollData(
					jpql.toString(), params.toArray(), orderby).getResultList();
			for (CarBrand brand : brands) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("cbid", brand.getId());
				map.put("name", brand.getName());
				map.put("fletter", brand.getJp());
				map.put("cbimage", brand.getImgPath());
				list.add(map);
			}
			dataMap.put("result", "001");
			dataMap.put("list", list);
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/***
	 * 获取车系信息
	 */
	@RequestMapping(value = "/server/carseries", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String carseries(@RequestParam(required = false) String uid,
			@RequestParam(required = false) String token, String cbid) {
		try {
			log.info("App【获取车系信息】：" + "uid=" + uid + " token=" + token
					+ " 品牌cbid=" + cbid);
			new MappingJackson2HttpMessageConverter();
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			/** 顶级车系 数据获取 **/
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuffer jpql = new StringBuffer("o.visible=?1 and o.level=?2");
			List<Object> params = new ArrayList<Object>();
			params.add(true);
			params.add(1);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<CarType> topCarTypes = carTypeService.getScrollData(
					jpql.toString(), params.toArray(), orderby).getResultList();
			for (CarType topCarType : topCarTypes) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("topname", topCarType.getName());
				// 获取顶级车系下的子车系
				List<CarType> types = carTypeService.listByTopID(topCarType
						.getId());
				List<Map<String, Object>> csList = new ArrayList<Map<String, Object>>();
				for (CarType type : types) {
					Map<String, Object> ctMap = new LinkedHashMap<String, Object>();
					ctMap.put("csid", type.getId());
					ctMap.put("name", type.getName());
					csList.add(ctMap);
				}
				map.put("cslist", csList);
				list.add(map);
			}
			dataMap.put("result", "001");
			dataMap.put("list", list);
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/***
	 * 获取车型信息
	 */
	@RequestMapping(value = "/server/carmodel", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String carmodel(@RequestParam(required = false) String uid,
			@RequestParam(required = false) String token, String csid) {
		try {
			log.info("App【获取车型信息】：" + "uid=" + uid + " token=" + token
					+ " 车系csid=" + csid);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			/** 车型 数据获取 **/
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuffer jpql = new StringBuffer(
					"o.visible=?1 and o.carType.id=?2");
			List<Object> params = new ArrayList<Object>();
			params.add(true);
			params.add(csid);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<Car> cars = carService.getScrollData(jpql.toString(),
					params.toArray(), orderby).getResultList();
			for (Car car : cars) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("cmid", car.getId());
				map.put("name", car.getName());
				list.add(map);
			}
			dataMap.put("result", "001");
			dataMap.put("list", list);
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/**
	 * 获取贵阳市 下属所有区信息列表
	 * 
	 * @param uid
	 * @param sid
	 * @param scid
	 * @return
	 */
	@RequestMapping(value = "/server/arealist", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String arealist(@RequestParam(required = false) String uid,
			@RequestParam(required = false) String token) {
		try {
			log.info("App【获取贵阳市 下属地区信息】：" + "uid=" + uid + " token=" + token);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			/** 区信息 数据获取 **/
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuffer jpql = new StringBuffer(
					"o.visible=?1 and o.parent.name=?2");
			List<Object> params = new ArrayList<Object>();
			params.add(true);
			params.add("贵阳市");// 待进一步确定
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<Area> areas = areaService.getScrollData(jpql.toString(),
					params.toArray(), orderby).getResultList();
			for (Area area : areas) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("aid", area.getId());
				map.put("name", area.getName());
				list.add(map);
			}
			dataMap.put("result", "001");
			dataMap.put("list", list);
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	@RequestMapping(value = "/server/streetlist", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String streetlist(@RequestParam(required = false) String uid,
			@RequestParam(required = false) String token, String aid) {
		try {
			log.info("App【获取贵阳市 下属地区信息】：" + "uid=" + uid + " token=" + token
					+ " 区ID=" + aid);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			/** 区信息 数据获取 **/
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuffer jpql = new StringBuffer(
					"o.visible=?1 and o.parent.id=?2");
			List<Object> params = new ArrayList<Object>();
			params.add(true);
			params.add(aid);// 待进一步确定
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<Area> areas = areaService.getScrollData(jpql.toString(),
					params.toArray(), orderby).getResultList();
			for (Area area : areas) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("sid", area.getId());
				map.put("name", area.getName());
				list.add(map);
			}
			dataMap.put("result", "001");
			dataMap.put("list", list);
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/***
	 * 获取预约时间
	 * 
	 * @param date
	 *            预约日期
	 * @param sid
	 *            街道ID
	 * @param scid
	 *            服务类型
	 * @return
	 */
	@RequestMapping(value = "/server/subscribe", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String subscribe(@RequestParam(required = false) String uid,
			@RequestParam(required = false) String token, String sid,
			String scid, String date) {
		try {
			log.info("App【获取预约时间】：" + "uid=" + uid + " token=" + token
					+ " sid=" + sid + " scid=" + scid + " date=" + date);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date nowDate = sdf.parse(date);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			/** 数据获取 **/
			List<DateMapBean> dmbs = orderService.getDateMapBean(sid, nowDate,
					scid);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			SimpleDateFormat timeSDF = new SimpleDateFormat("HH:mm");
			for (DateMapBean dmb : dmbs) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("stime", timeSDF.format(dmb.getStartDate()));
				map.put("etime", timeSDF.format(dmb.getEndDate()));
				list.add(map);
			}
			dataMap.put("result", "001");
			dataMap.put("list", list);
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/***
	 * 获取商品品牌
	 * 
	 */
	@RequestMapping(value = "/product/brand", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String productBrand(@RequestParam(required = false) String uid,
			@RequestParam(required = false) String token,
			@RequestParam(required = false) String scid) {
		try {
			log.info("App【获取商品品牌】：" + "uid=" + uid + " token=" + token
					+ " scid=" + scid);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			/** 数据获取 **/
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuffer jpql = new StringBuffer("o.visible=?1");
			List<Object> params = new ArrayList<Object>();
			params.add(true);
			List<ProductBrand> brands = productBrandService.getScrollData(
					jpql.toString(), params.toArray(), orderby).getResultList();

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (ProductBrand brand : brands) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("bid", brand.getId());
				map.put("bname", brand.getName());
				map.put("bimage", brand.getImgPath());
				list.add(map);
			}
			dataMap.put("result", "001");
			dataMap.put("list", list);
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/***
	 * 根据服务类型 获取商品类型
	 * 
	 */
	@RequestMapping(value = "/product/category", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String productCategory(@RequestParam(required = false) String uid,
			@RequestParam(required = false) String token, String scid) {
		try {
			log.info("App【获取商品类型】：" + "uid=" + uid + " token=" + token
					+ " scid=" + scid);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			/** 数据获取 **/
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuffer jpql = new StringBuffer(
					"o.visible=?1 and o.serviceType.id=?2");
			List<Object> params = new ArrayList<Object>();
			params.add(true);
			params.add(scid);
			List<ProductCategory> categorys = productCategoryService
					.getScrollData(jpql.toString(), params.toArray(), orderby)
					.getResultList();

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (ProductCategory pc : categorys) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("pcid", pc.getId());
				map.put("pcame", pc.getName());
				list.add(map);
			}
			dataMap.put("result", "001");
			dataMap.put("list", list);
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/***
	 * 获取默认商品
	 * 
	 */
	@RequestMapping(value = "/product/default", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String productDefault(@RequestParam(required = false) String uid,
			@RequestParam(required = false) String token, String scid,
			String cmid) {
		try {
			log.info("App【获取商品类型】：" + "uid=" + uid + " token=" + token
					+ " scid=" + scid + " cmid=" + cmid);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			/** 数据获取 **/
			Product product = productService.defaultForServer(scid, cmid);
			if (product != null) {
				dataMap.put("result", "001");
				dataMap.put("pid", product.getId());
				dataMap.put("pame", product.getName());
				dataMap.put("pimage", product.getImgPath());
				dataMap.put("pbuynum", 12);// 商品购买数
				dataMap.put("price", product.getPrice());
				dataMap.put("pcomts", 200);
				dataMap.put("result", "001");
				String json = mapper.writeValueAsString(dataMap);
				return json;
			} else {
				dataMap.put("result", "001");
				dataMap.put("pid", "");
				dataMap.put("pame", "");
				dataMap.put("pimage", "");
				dataMap.put("pbuynum", 0);
				dataMap.put("price", 0);
				dataMap.put("pcomts", 0);
				String json = mapper.writeValueAsString(dataMap);
				return json;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/***
	 * 获取商品列表
	 * 
	 */
	@RequestMapping(value = "/product/list", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String productList(@RequestParam(required = false) String uid,
			@RequestParam(required = false) String token, String scid,
			String cmid, @RequestParam(required = false) String[] pids,
			int page, int maxresult) {
		try {
			log.info("App【获取商品类型】：" + "uid=" + uid + " token=" + token
					+ " scid=" + scid + " cmid=" + cmid + " pids length="
					+ pids.length);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			/** 数据获取 **/
			PageView<Product> pageView = new PageView<Product>(maxresult, page);
			pageView.setTotalrecord(productService.countForServer(scid, cmid,
					pids));
			pageView.setRecords(productService.listForServer(scid, cmid,
					pageView.getFirstResult(), maxresult, pids));

			List<Product> products = pageView.getRecords();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (Product product : products) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("pid", product.getId());
				map.put("pame", product.getName());
				map.put("pimage", product.getImgPath());
				map.put("pbuynum", 12);// 商品购买数
				map.put("price", product.getPrice());
				map.put("pcomts", 200);
				list.add(map);
			}
			dataMap.put("result", "001");
			dataMap.put("list", list);
			String json = mapper.writeValueAsString(dataMap);

			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/**
	 * 查询是否有空闲的技师
	 * 
	 * @param uid
	 * @param token
	 * @param scid
	 * @param bid
	 * @param pcid
	 * @param cmid
	 * @return
	 */
	@RequestMapping(value = "/server/Technician", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String serverTechnician(@RequestParam(required = false) String uid,
			@RequestParam(required = false) String token) {
		try {
			log.info("App【查询是否有空闲技师】：" + "uid=" + uid + " token=" + token);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();

			dataMap.put("result", "001");
			dataMap.put("canserver", 1);// 暂时设定为1
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/**
	 * 提交洗车订单
	 * 
	 * @param uid
	 * @param token
	 * @param ucid
	 *            用户车辆信息ID
	 * @param scid
	 *            服务类型
	 * @param contact
	 *            联系方式
	 * @param phone
	 *            联系电话
	 * @param plateNumber
	 *            车牌号
	 * @param color
	 *            车身颜色
	 * @param lat
	 *            纬度
	 * @param lng
	 *            经度
	 * @param address
	 *            详细地址
	 * @param cpid
	 *            优惠券ID
	 * @param cleanType
	 *            清洗方式
	 * @param payWay
	 *            支付方式
	 * @return
	 */

	@RequestMapping(value = "/order/carwash/save", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String orderCarWashSave(String uid, String token, String ucid, String scid,
			String contact, String phone, String plateNumber, String color,
			double lat, double lng, String address, int cleanType, int payWay,
			@RequestParam(required = false) String cpid) {
		try {
			log.info("App【提交洗车订单】：" + "uid=" + uid + " token=" + token
					+ " ucid=" + ucid + " scid=" + scid + " contract="
					+ contact + " phone=" + phone + " plateNumber="
					+ plateNumber + " color=" + color + " lat=" + lat + " lng="
					+ lng + " address=" + address + " cleanType=" + cleanType
					+ " payWay=" + payWay + " cpid=" + cpid);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

			/** 数据获取 **/
			Member member = memberService.find(uid);
			UserCar userCar = userCarService.find(ucid);
			ServiceType serviceType = serviceTypeService.find(scid);

			Order order = new Order();
			order.setContact(contact);
			order.setPhone(phone);
			order.setType(1);// 表示是洗车订单
			order.setAddress(address);
			order.setServerWay(1);// 上门服务
			order.setPayWay(payWay);
			order.setCleanType(cleanType);
			order.setMember(member);
			order.setCar(userCar.getCar());
			/* 存储服务类型 */
			ServerOrder serverOrder = new ServerOrder();
			serverOrder.setName(serviceType.getName());
			serverOrder.setServiceType(serviceType);
			serverOrder.setOrder(order);
			order.getServerOrder().add(serverOrder);
			orderService.save(order);
			orderService.bindTechnician(order.getId()); // 分配技师
			dataMap.put("result", "001");
			dataMap.put("oid", order.getId());
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/**
	 * 提交保养订单
	 * 
	 * @param uid
	 * @param token
	 * @param cmid
	 *            车型ID
	 * @param stime
	 *            预约时间
	 * @param phone
	 *            联系电话
	 * @param contact
	 *            联系人
	 * @param address
	 *            详细地址
	 * @param payWay
	 *            支付方式
	 * @param plateNumber
	 *            车牌号
	 * @param color
	 *            车辆颜色
	 * @param httpData
	 *            json数据格式
	 *            {"scid1":{"p1":1,"p2":2},"scid2":{"p3":2,"p4":3,"p5":3}}
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/order/save", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String orderServerSave(String uid, String token, String ucid, String stime,
			String contact, String phone, String plateNumber, String color,
			double lat, double lng, String address, int payWay,
			String httpData, @RequestParam(required = false) String cpid) {
		try {
			log.info("App【提交保养订单】：" + "uid=" + uid + " token=" + token
					+ " ucid=" + ucid + " stime=" + stime + " contract="
					+ contact + " phone=" + phone + " plateNumber="
					+ plateNumber + " color=" + color + " lat=" + lat + " lng="
					+ lng + " address=" + address + " payWay=" + payWay
					+ " httpData=" + httpData + " cpid=" + cpid);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

			/** 数据获取 **/
			Member member = memberService.find(uid);
			UserCar userCar = userCarService.find(ucid);
			Car car = userCar.getCar();

			Order order = new Order();
			order.setStartDate(sdf.parse(stime));
			order.setPhone(phone);
			order.setContact(contact);
			order.setAddress(address);
			order.setServerWay(1);
			order.setPayWay(payWay);
			order.setMember(member);
			order.setCar(car);
			// order.setServiceType(serviceType);
			/** 解析httpData Json数据包 **/
			Map<String, Map<String, Integer>> packMap = mapper.readValue(
					httpData, Map.class);
			for (String scid : packMap.keySet()) {
				ServiceType serviceType = serviceTypeService.find(scid);
				ServerOrder so = new ServerOrder();
				so.setName(serviceType.getName());
				so.setServiceType(serviceType);
				so.setOrder(order);
				order.getServerOrder().add(so); // 添加对应服务
				Map<String, Integer> pmap = packMap.get(scid);
				for (String pid : pmap.keySet()) {
					Product product = productService.find(pid);
					ServerOrderDetail sod = new ServerOrderDetail();
					sod.setPrice(product.getPrice());
					sod.setName(product.getName());
					sod.setServerOrder(so);
					sod.setOrder(order);
					order.getServerOrderDetail().add(sod);
				}
			}
			orderService.save(order);
			dataMap.put("result", "001");
			dataMap.put("oid", order.getId());
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/**
	 * 获取用户的车辆信息列表
	 */
	@RequestMapping(value = "/user/cars", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String userCarList(String uid, String token) {
		try {
			log.info("App【获取用户所有车辆信息列表】：" + "uid=" + uid + " token=" + token);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			/** 数据获取 */
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuffer jpql = new StringBuffer(
					"o.visible=?1 and o.member.id=?2");
			List<Object> params = new ArrayList<Object>();
			params.add(true);
			params.add(uid);
			List<UserCar> userCars = userCarService.getScrollData(
					jpql.toString(), params.toArray(), orderby).getResultList();

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (UserCar userCar : userCars) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				Car car = userCar.getCar();
				CarType carType = car.getCarType();
				CarBrand carBrand = carType.getCarBrand();

				map.put("ucid", userCar.getId());
				map.put("defaultCar", userCar.getDefaultCar() ? 1 : 0);
				map.put("color", userCar.getCarColor());
				map.put("plateNumber", userCar.getCarNum());
				map.put("fuel", userCar.getFuel());
				map.put("drange", userCar.getDrange());
				map.put("engines", userCar.getEngines());
				if (userCar.getRoadDate() != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					map.put("date", sdf.format(userCar.getRoadDate()));
				} else {
					map.put("date", "");
				}
				map.put("cmid", car.getId());
				map.put("cmname", car.getName());
				map.put("csid", carType.getId());
				map.put("csname", carType.getName());
				map.put("cbid", carBrand.getId());
				map.put("cbname", carBrand.getName());
				list.add(map);
			}
			dataMap.put("result", "001");
			dataMap.put("list", list);

			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/**
	 * 获取用户默认车辆信息
	 */
	@RequestMapping(value = "/user/car", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String userCarDefault(String uid, String token) {
		try {
			log.info("App【获取用户默认车辆信息】：" + "uid=" + uid + " token=" + token);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			/** 获取数据 */
			UserCar userCar = userCarService.defaultCar(uid);
			if (userCar == null) {
				dataMap.put("result", "104"); //
				dataMap.put("ucid", "");
				dataMap.put("defaultCar", 1);
				dataMap.put("colore", "");
				dataMap.put("plateNumber", "");
				dataMap.put("fuel", 0);
				dataMap.put("drange", 0);
				dataMap.put("engines", "");
				dataMap.put("date", "");
				dataMap.put("cmid", "");
				dataMap.put("cmname", "");
				dataMap.put("csid", "");
				dataMap.put("csname", "");
				dataMap.put("cbid", "");
				dataMap.put("cbname", "");
				String json = mapper.writeValueAsString(dataMap);
				return json;
			}
			Car car = userCar.getCar();
			CarType carType = car.getCarType();
			CarBrand carBrand = carType.getCarBrand();
			dataMap.put("result", "001");

			dataMap.put("ucid", userCar.getId());
			dataMap.put("defalutCar", 1);
			dataMap.put("color", userCar.getCarColor());
			dataMap.put("plateNumber", userCar.getCarNum());
			dataMap.put("fuel", userCar.getFuel());
			dataMap.put("drange", userCar.getDrange());
			dataMap.put("engines", userCar.getEngines());
			if (userCar.getRoadDate() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				dataMap.put("date", sdf.format(userCar.getRoadDate()));
			} else {
				dataMap.put("date", "");
			}
			dataMap.put("cmid", car.getId());
			dataMap.put("cmname", car.getName());
			dataMap.put("csid", carType.getId());
			dataMap.put("csname", carType.getName());
			dataMap.put("cbid", carBrand.getId());
			dataMap.put("cbname", carBrand.getName());
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/**
	 * 修改用户的车型信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/user/carsave", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String userCarSave(String uid, String token, String ucid, String cmid,
			int defaultCar, @RequestParam(required = false) String color,
			@RequestParam(required = false) String plateNumber,
			@RequestParam(required = false) Double fuel,
			@RequestParam(required = false) Double drange,
			@RequestParam(required = false) String engines,
			@RequestParam(required = false) String date) {
		try {
			log.info("App【保存用户当前的车型】：" + "uid=" + uid + " token=" + token
					+ "cmid=" + cmid + " fule=" + fuel + " drange=" + drange
					+ " engines=" + engines + " date=" + date);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Member member = memberService.find(uid);
			if (member == null) {
				dataMap.put("result", "106"); // 用户ID错误
				String json = mapper.writeValueAsString(dataMap);
				return json;
			}
			/** 保存数据 */
			UserCar userCar = new UserCar();
			if (color != null && !"".equals(color)) {
				userCar.setCarColor(color);
			}
			if (plateNumber != null && !"".equals(plateNumber)) {
				userCar.setCarNum(plateNumber);
			}
			if (defaultCar == 1) {// 重置该用户所有车为非默认车
				userCarService.resetUndefault(uid);
				userCar.setDefaultCar(true);
			}
			if (fuel != null && fuel > 0) {
				userCar.setFuel(fuel);
			}
			if (drange != null && drange > 0) {
				userCar.setDrange(drange);
			}
			if (engines != null && !"".equals(engines)) {
				userCar.setEngines(engines);
			}
			if (date != null && !"".equals(date)) {
				userCar.setRoadDate(sdf.parse(date));
			}
			Car car = carService.find(cmid);
			if (car != null) {
				userCar.setCar(car);
			}
			userCar.setMember(member);
			userCarService.save(userCar);
			dataMap.put("result", "001");
			dataMap.put("ucid", userCar.getId());
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/**
	 * 修改用户的车型信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/user/caredit", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String userCarEdit(String uid, String token, String ucid, String cmid,
			int defaultCar, @RequestParam(required = false) String color,
			@RequestParam(required = false) String plateNumber,
			@RequestParam(required = false) Double fuel,
			@RequestParam(required = false) Double drange,
			@RequestParam(required = false) String engines,
			@RequestParam(required = false) String date) {
		try {
			log.info("App【修改用户当前的车型】：" + "uid=" + uid + " token=" + token
					+ "cmid=" + cmid + " fule=" + fuel + " drange=" + drange
					+ " engines=" + engines + " date=" + date);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			/** 数据获取 **/
			UserCar userCar = userCarService.find(ucid);
			if (userCar == null) { // 没有对应的车辆信息
				dataMap.put("result", "104");
				String json = mapper.writeValueAsString(dataMap);
				return json;
			}
			if (!userCar.getMember().getId().equals(ucid)) {// 非自己的车
				dataMap.put("result", "105");
				String json = mapper.writeValueAsString(dataMap);
				return json;
			}

			if (color != null && !"".equals(color)) {
				userCar.setCarColor(color);
			}
			if (plateNumber != null && !"".equals(plateNumber)) {
				userCar.setCarNum(plateNumber);
			}
			if (defaultCar == 1) {// 重置该用户所有车为非默认车
				userCarService.resetUndefault(uid);
				userCar.setDefaultCar(true);
			}
			if (fuel != null && fuel > 0) {
				userCar.setFuel(fuel);
			}
			if (drange != null && drange > 0) {
				userCar.setDrange(drange);
			}
			if (engines != null && !"".equals(engines)) {
				userCar.setEngines(engines);
			}
			if (date != null && !"".equals(date)) {
				userCar.setRoadDate(sdf.parse(date));
			}
			Car car = carService.find(cmid);
			if (car != null) {
				userCar.setCar(car);
			}
			userCarService.update(userCar);
			dataMap.put("result", "001");
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	@RequestMapping(value = "/extra/news", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String extraNews(String uid, String token, int page, int maxresult) {
		try {
			log.info("App【获取新闻列表】：" + "uid=" + uid + " token=" + token
					+ "page=" + page + " maxresult=" + maxresult);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			/** 数据获取 **/
			PageView<News> pageView = new PageView<News>(maxresult, page);
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuffer jpql = new StringBuffer("o.visible=?1");
			List<Object> params = new ArrayList<Object>();
			params.add(true);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			pageView.setQueryResult(newsService.getScrollData(
					pageView.getFirstResult(), maxresult, jpql.toString(),
					params.toArray(), orderby));
			List<News> newsList = pageView.getRecords();
			for (News nw : newsList) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("nwid", nw.getId());
				map.put("nwtitle", nw.getTitle());
				map.put("nwimage", nw.getImgPath());
				map.put("nwdate", sdf.format(nw.getCreateDate()));
				map.put("nwstar", 12);// 暂时无此属性
				map.put("nwhtml", "");// ...
				list.add(map);
			}
			dataMap.put("result", "001");
			dataMap.put("pages", pageView.getTotalPage());
			dataMap.put("list", list);
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	@RequestMapping("/test")
	public @ResponseBody
	String test() {
		String[] pids = { "123", "345" };
		productService.listForServer("sc2",
				"65377a5e-081f-4a28-a5ba-1d80c53932a4", 0, 20, null);
		return "test";
	}

	/**
	 * 发送验证码
	 * 
	 * @param phone
	 * @param type
	 * @return
	 */
	@RequestMapping("/common/icode")
	public @ResponseBody
	String icode(String phone, int type) {
		try {
			log.info("App【验证码发送】：" + "电话=" + phone + " type=" + type);
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			// 发送时间间隔判断
			ShortMessage lastSMS = shortMessageService.lastSMS(phone, type);
			if (lastSMS != null) {
				long timegaps = System.currentTimeMillis()
						- lastSMS.getCreateDate().getTime();
				if (timegaps < 60 * 1000) {
					dataMap.put("result", "603");
					String json = mapper.writeValueAsString(dataMap);
					return json;
				}
			}
			// 发送时间间隔大于1分钟，执行发送相关程序
			ShortMessage message = new ShortMessage();
			// message.setCode(IDGenerator.getRandomString(4, 1));
			message.setCode("1234");
			message.setType(type);
			message.setPhone(phone);
			if (type == 1) {
				message.setContent("【一动车保】您本次登录的验证码为：" + message.getCode()
						+ "，验证码5分钟内有效。");
			}
			boolean sendResult = sendSms(phone, message.getContent());
			shortMessageService.save(message);

			dataMap.put("result", "001");
			String json = mapper.writeValueAsString(dataMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"000\"}";
		}
	}

	/**
	 * 检查令牌是否有效
	 * 
	 * @param uid
	 * @param authId
	 * @return
	 */
	private boolean checkToken(String uid, String authId) {
		Token token = tokenService.findToken(uid, authId);
		if (token != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 发送短信
	 * 
	 * @param phone
	 *            接受人手机号
	 * @param content
	 *            短信内容
	 * @return
	 */
	private boolean sendSms(String phone, String content) {
		String path = "http://www.duanxin10086.com/sms.aspx";// 地址
		Map<String, String> params = new HashMap<String, String>();
		params.put("action", "send");
		params.put("userid", "11760");
		params.put("account", "一动车保");
		params.put("password", "123456");
		params.put("mobile", phone);// 接受人电话
		params.put("content", content);// 短信内容
		boolean sendResult = false;
		try {
			String result = this.sendGetRequest(path, params, "UTF-8");
			if (result.contains("<returnstatus>Success</returnstatus>")
					&& result.contains("<message>ok</message>")) {
				sendResult = true;
			} else {
				sendResult = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			sendResult = false;
		}
		return sendResult;
	}

	/***
	 * 发送HTTP GET请求
	 * 
	 * @param path
	 * @param params
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	private String sendGetRequest(String path, Map<String, String> params,
			String encoding) throws Exception {
		StringBuffer url = new StringBuffer(path);
		url.append("?");
		for (Map.Entry<String, String> entry : params.entrySet()) {
			url.append(entry.getKey());
			url.append("=");
			url.append(URLEncoder.encode(entry.getValue(), encoding));
			url.append("&");
		}
		url.deleteCharAt(url.length() - 1);
		HttpURLConnection conn = (HttpURLConnection) new URL(url.toString())
				.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {
			InputStream in = conn.getInputStream();
			String result = IOUtils.toString(in);
			return result;
		} else {
			throw new Exception("connection fail");
		}
	}

	public static void main(String[] args) {
		try {
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();

			Map<String, Integer> map1 = new LinkedHashMap<String, Integer>();
			map1.put("p1", 1);
			map1.put("p2", 2);

			Map<String, Integer> map2 = new LinkedHashMap<String, Integer>();
			map2.put("p3", 2);
			map2.put("p4", 3);
			map2.put("p5", 3);

			dataMap.put("scid1", map1);
			dataMap.put("scid2", map2);
			String httpData = mapper.writeValueAsString(dataMap);
			System.out.println(httpData);

			Map<String, Map<String, Integer>> packMap = mapper.readValue(
					httpData, Map.class);
			for (String scid : packMap.keySet()) {
				Map<String, Integer> pmap = packMap.get(scid);
				for (String pid : pmap.keySet()) {
					System.out.println(scid + "#" + pid + "#" + pmap.get(pid));
				}
			}
			// String [] value = rmap.get(key);
			// System.out.println(key+":"+value);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
