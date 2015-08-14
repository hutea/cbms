<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/page/common/taglib.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
	<title>一动车保首页</title>
	<link href="<%=basePath %>resource/page/css/style.css" type="text/css" rel="stylesheet" />	
	<script type="text/javascript" src="<%=basePath %>resource/page/js/jquery.js"></script>
	<style type="text/css">
		/******form*******/
		.divselectcon {clear: both; padding: 10px 0 20px; position: relative; }
		.divselect {float: left; }
		.divselectcon input {width:238px; height: 35px; margin: 0 20px; position:relative; z-index:10000; border: 1px solid #b1b1b1; border-top: 2px solid #b1b1b1; line-height:35px; display:inline-block; color:#807a62; font-style:normal; background:url(images/corners2.png) no-repeat right center;}
		.divselectcon input.input1 {border:1px solid #ffae00; border-top: 2px solid #ffae00; margin-left: 0; width: 228px; height:35px;line-height:35px; display:inline-block; color:#807a62; font-style:normal; background:url(images/corners1.png) no-repeat right center;}
		#button2 button {float: right; width: 169px; height: 43px; background: url(images/go.png) 0 0 no-repeat; border: none; position: absolute; top: 9px;  cursor: pointer; }
		.bdsug_copy{display:none;}		
	</style>
	<script type="text/javascript">
		function history(){
			$("#couponHistoryForm").submit();
		}
		
		function convertCoupon(couponId){
			var url ="<%=basePath%>user/myCoupon/convert";
			var data = {
				memberId : "${sessionScope.member_session.id }",
				id : couponId
			};
			$.post(url,data,function(result){
				alert(result.message);
				parent.location.reload();
			},"json");
		}
	</script>
</head>

<body>
<!--上部开始-->
<jsp:include page="../header.jsp"/>
<!--上部结束-->
<hr>
<!--中部开始-->
<div class="box3">
<div class="mid box0">
	<div class="steward1-content">
		<div class="steward1-menu" id="steward1-menu">
			<div class="menu-title menu-order active">
				<h3>订单中心</h3>
			</div>
			<ul class="menu-list">
				<li class="menu-item myOrder">
					<a href="javascript:;">我的订单</a>
				</li>
				<li class="menu-item cancelOrder">
					<a href="javascript:;">已取消的订单</a>
				</li>
				<li class="menu-item myCoupon on">
					<a href="javascript:;">我的优惠券</a>
				</li>
			</ul>
			<div class="menu-title menu-home">
				<h3>我的车管家</h3>
			</div>
			<ul class="menu-list">
				<li class="menu-item myCarModel">
					<a href="javascript:;">我的车型库</a>
				</li>
			</ul>
			<div class="menu-title menu-center">
				<h3>账户中心</h3>
			</div>
			<ul class="menu-list">
				<li class="menu-item accountBal">
					<a href="javascript:;">账户余额</a>
				</li>
				<li class="menu-item basicInfo">
					<a href="javascript:;">基本信息</a>
				</li>
				<li class="menu-item changePass">
					<a href="javascript:;">修改密码</a>
				</li>
				<li class="menu-item feedBack">
					<a href="javascript:;">意见反馈</a>
				</li>
			</ul>
		</div>
		<div class="steward1-detail" id="steward1-detail">
			<div id="myOrderContent" class="myOrderContent myOrderDetail" style="border: none; ">00000000000000000000000000000000</div>
			<div id="cancelOrderContent" class="cancelOrderContent myOrderDetail" style="border: none; ">1111111111111111111111111111</div>
			<div id="myCouponContent" class="myCouponContent myOrderDetail">
				<div class="orderDetails">
					<div class="orderDetailsTop">
						<div class="left">
							<p class="coupons">当前积分:<i><fmt:formatNumber value="${amount }" type="number" pattern=""/></i>，下面是可兑换的优惠券</p>
						</div>
						<form action="<%=basePath%>user/myCoupon/history" id="couponHistoryForm" method="post">
							<input type="hidden" name="memberId" value="${sessionScope.member_session.id }">
						</form>
						<div class="right"><a href="JavaScript:history()">查看历史记录&gt;&gt;</a></div>
					</div>
					<div class="orderDetailsContent">
						<ul class="couponsList">
							<c:forEach items="${coupons }" var="coupon" >
								<li><a href="javascript:convertCoupon('${coupon.id }');"> <img src="<%=basePath %>${coupon.imgPath }" /></a></li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
			<div id="myCarModelContent" class="myCarModelContent myOrderDetail">333333333333333333333333333</div>
			<div id="accountBalContent" class="accountBalContent myOrderDetail">444444444444444444444444444-1-1-1-1--1-1-1-1-1</div>
			<div id="basicInfoContent" class="basicInfoContent myOrderDetail">55555555555555555555555555555555555555555555555444444444444</div>
			<div id="changePassContent" class="changePassContent myOrderDetail">66666666666666666666666666666666666666666666665555555555555555555555555555555555555555555555</div>
			<div id="feedBackContent" class="feedBackContent myOrderDetail">777777777777777777777777777777777777777777777-2-2-2-2-2-2-2--2-2-2-2</div>
		</div>
	</div>
	
	
</div>
</div>
<!--中部结束-->
<!--底部开始-->
<jsp:include page="../footer.jsp"/>
<!--底部结束-->

<script>
	$(function(){
	function tabs(tabTit,on,active,tabCon){ 
		$(tabCon).each(function(){ 
			$(this).children().eq(2).show();  
		});
		$(tabTit).each(function(){
			//$(this).children().eq(2).addClass(on);
		});
		$(tabTit).children().click(function(){ 
			$(this).addClass(on).siblings().removeClass(on);
			var index = $(tabTit).children().index(this);
			$(tabCon).children().eq(index).show().siblings().hide();
			$(this).siblings().addClass(on);			
			$(this).siblings().removeClass(on);
			$(this).parent().siblings().children().removeClass(on);
			
			$(this).parent().siblings().removeClass(active);
			$(this).parent().prev().addClass(active);			
		});
	}	
	tabs(".menu-list","on","active","#steward1-detail");
	})


	$(function(){
		function tabs(tabTit,liSelected,tabCon){ 
			$(tabCon).each(function(){ 
				$(this).children().eq(0).show();  
			});
			$(tabTit).each(function(){
				//$(this).children().eq(0).addClass(liSelected);
			});
			$(tabTit).children().click(function(){ 
				$(this).addClass(liSelected).siblings().removeClass(liSelected);
				var index = $(tabTit).children().index(this);
				$(tabCon).children().eq(index).show().siblings().hide();
				$(this).siblings().addClass(liSelected);			
				$(this).siblings().removeClass(liSelected);
				$(this).parent().siblings().children().removeClass(liSelected);				
			});
		}
		tabs(".totalTitle","liSelected",".orderListsContent");
	})
	
	
			
	$(document).ready(function(){
		$(".selectInput").focus(function(){
			$(this).addClass("selected");
		});
		$(".selectInput").blur(function(){
			$(this).removeClass("selected");
		});
	});
		
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
		 
			$('input').each(function(){
		 
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