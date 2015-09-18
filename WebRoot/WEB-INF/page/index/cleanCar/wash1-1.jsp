<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/page/common/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
	<title>一动车保首页</title>
<link href="${pageContext.request.contextPath}/resource/page/css/style.css" type="text/css" rel="stylesheet" />	
<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resource/chain/js/laydate/need/laydate.css"> --%>
<%-- <link href="<%=basePath%>resource/page/css/style.main3.css" type="text/css" rel="stylesheet" /> --%>
<script src="${pageContext.request.contextPath}/resource/chain/js/jquery-1.11.1.min.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/common.other.js"></script>
	<style type="text/css">
		/******form*******/
		.divselectcon {clear: both; padding: 10px 0 20px; position: relative; }
		.divselect {float: left; }
		.divselectcon input {width:238px; height: 35px; margin: 0 20px; position:relative; z-index:10000; border: 1px solid #b1b1b1; border-top: 2px solid #b1b1b1; line-height:35px; display:inline-block; color:#807a62; font-style:normal; background:url(images/corners2.png) no-repeat right center;}
		.divselectcon input.input1 {border:1px solid #ffae00; border-top: 2px solid #ffae00; margin-left: 0; width: 228px; height:35px;line-height:35px; display:inline-block; color:#807a62; font-style:normal; background:url(images/corners1.png) no-repeat right center;}
		#button2 button {float: right; width: 169px; height: 43px; background: url(${pageContext.request.contextPath}/resource/page/images/go.png) 0 0 no-repeat; border: none; position: absolute; top: 9px;  cursor: pointer; }
		.bdsug_copy{display:none;}		
	</style>
</head>

<body>
<!--上部开始-->
<div class="top">
	<div class="top_header box0">
		<div class="logo"><img src="images/top_logo.png" alt="logo" /></div>
		<ul class="top_right">
			<li><a href="#">[<span>登录</span>|<span>注册</span>]</a><b class="b1"></b></li>
			<li class=""><a href="#">新闻动态</a><b class="b22"></b></li>
			<li class=""><a href="#">服务范围</a><b class="b33"></b></li>
			<li class=""><a href="#">APP下载</a><b class="b44"></b></li>
		</ul>
	</div>
	<!--box banner start-->
	<!--box banner end-->
</div>
<!--上部结束-->
<hr>
<!--中部开始-->
<div class="mid box0">
	<div class="shopping">
		<ul class="steps_2">
			<li class="b_li1"><a href="#"><span>01.</span>确定您的车型</a></li>
			<li class="b_li2 stepColor"><a href="#"><span>02.</span>订单信息</a></li>
			<li class="b_li3"><a href="#"><span>03.</span>核对订单信息</a></li>
			<li class="b_li3"><a href="#"><span>04.</span>下单成功</a></li>
		</ul>
		<div class="shopping_con">
			<div class="main_1">
				<div class="shopping_con_top">
					<div class="text">
						<a href="#"><img src="images/buy_1.png" /></a>
						<p>奥迪&nbsp;&nbsp;Q7 (进口)&nbsp;&nbsp;3.0T(35TFSI)&nbsp;&nbsp;2013年产</p>
						<a href="#"><img src="images/buy_2.png" alt="" /></a>
					</div>
					<div class="carColor"><input type="text" value="白色" readonly="readonly" /></div>
					<div class="carNum"><input type="text" /></div>
				</div>
				<h2>清洗方式和地址</h2>
				<div class="main_11">
					<h3>清洗方式：</h3>
					<div class="clearMethodCon">
						<div class="clearMethod">
							<label class="selectClear">外观清洗</label>
							<div class="gouimg"></div>
						</div>
						<div class="clearMethod">
							<label class="">内外清洗</label>
							<div class="gouimg display"></div>
						</div>
					</div>
				</div>
				<script>
				function bindElement(){
					$(".clearMethod").bind("click",".clearMethod",function(){
						$(".clearMethod").find("label").removeClass("selectClear");
						$(".clearMethod").find(".gouimg").addClass("display");
						
						$(this).find("label").addClass("selectClear");
						$(this).find(".gouimg").removeClass("display");
					});
				}
				bindElement();				
				</script>
				<div class="main_12">
					<h3>服务地址：</h3>
					<div class="addr"><a href="#"><img src="images/addr.png" /></a>贵阳市云岩区大营坡汪家湾路口（鹿冲关斜对面）</div>		
					<div class="addr_con">
						<a href="#"><img src="images/addr_con.png" /></a>
						<p>[服务范围设定为贵阳市观山湖区、白云区、南明区、云岩区、小河、花溪区、乌当区，暂不支持息烽、开阳、清镇、修文。]</p>
					</div>
					<div class="clearfloat"></div>
				</div>
			</div>
			<div class="mian_13">
				<div class="mian_2">
				<h2>联系人信息</h2>
				<div><label for="user"><span>用户姓名：</span><input type="text" name="user" class="user" id="user" /></label></div>
				<div><label for="tel"><span>联系电话：</span><input type="text" name="tel" class="tel" id="tel" /></label></div>
				</div>
			</div>
			<div class="ma_buy_total0">
				<div class="ma_buy_total_left">
					<h2>支付方式</h2>
					<form>
						<ul>
							<li><input type="radio" checked name="pay" id="pay1" /><label for="pay1"><img src="images/ma_9.png" alt="" /></label>
							</li>
							<li><input type="radio" name="pay" id="pay2" /><label for="pay2"><img src="images/ma_10.png" alt="" /></label>
							</li>
							<li><input type="radio" name="pay" id="pay3" /><label for="pay3" class="pay3">余额支付</label>
							</li>
						</ul>
					</form>
				</div>
				<div class="ma_buy_total_right">
					<div>
						<div class="ma_buy_1 ma_buy_11">服务费用：</div>
						<div class="ma_buy_2">￥35.00</div>
					</div>
					<div>
						<div class="ma_buy_1 ma_buy_12" id="coupons">
							<span>优惠券：</span>
							<input type="text" id="card" placeholder="满30元 减10元" />
						</div>
						<div class="ma_buy_2" style="width: 120px; right: -40px; ">-￥10.00</div>
					</div>
				</div>
			</div>
			<div class="buy_total2">
				<div class="buy_total2_right">
					<p>实付金额：<span>￥25.00</span></p>
					<button type="button"></button>
				</div>
			</div>
		</div>	
	</div>
</div>
<!--中部结束-->
<!--底部开始-->
<div class="down">	
	<div class="span5">
		<div class="box0">
			<div class="span5l">
				<div class="down_pic"><img src="images/down_pic.png" alt="" />
				</div>
				<div class="down_txt">
					<ul>
						<li><a href="#">一动车保</a>|</li>
						<li><a href="#">关于我们</a>|</li>
						<li><a href="#">服务简介</a>|</li>
						<li><a href="#">市场合作</a>|</li>
						<li><a href="#">联系我们</a></li>
					</ul>
					<p>&copy;2015&nbsp;www.YIDONGCHEBAO.com&nbsp; 贵ICP备18888888号</p>
				</div>
			</div>
			<div class="span5r">
				<ul>
					<li class="l1">咨询热线：400-855-9999</li>
					<li class="l2">咨询QQ：4008559999</li>
					<li class="l3">公司地址：贵州省贵阳市高新区688号</li>
				</ul>
				<div class="weixin">
					<img src="images/code.png" alt="" />
					<p>扫一扫有惊喜！</p>
				</div>
			</div>
		</div>
	</div>
</div>
<!--底部结束-->
<script>
$(function(){
//判断浏览器是否支持placeholder属性
supportPlaceholder='placeholder'in document.createElement('input'), 
  placeholder=function(input){
 
    var text = input.attr('placeholder'),
    defaultValue = input.defaultValue;
 
    if(!defaultValue){
 
      input.val(text).addClass("phcolor");
    }
 
    input.focus(function(){
 
      if(input.val() == text){
   
        $(this).val("");
      }
    });
 
  
    input.blur(function(){
 
      if(input.val() == ""){
       
        $(this).val(text).addClass("phcolor");
      }
    });
 
    //输入的字符不为灰色
    input.keydown(function(){
  
      $(this).removeClass("phcolor");
    });
  };
 
  //当浏览器不支持placeholder属性时，调用placeholder函数
  if(!supportPlaceholder){
 
    $('#coupons input').each(function(){
 
      text = $(this).attr("placeholder");
 
      if($(this).attr("type") == "text"){
 
        placeholder($(this));
      }
    });
  }
 
});
</script>
</body>
</html>