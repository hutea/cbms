<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/page/common/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String base = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
	<title>一动车保首页</title>
	<%-- <link href="${pageContext.request.contextPath}/resource/chain/css/style.default.css" rel="stylesheet"> --%>
	<link href="${pageContext.request.contextPath}/resource/chain/css/style.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/resource/page/js/jquery.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resource/page/js/function.js"></script>
	<style type="text/css">
		/******form*******/
		.divselectcon {clear: both; padding: 10px 0 20px; position: relative; }
		.divselect {float: left; }
		.divselectcon input {width:238px; height: 35px; margin: 0 20px; position:relative; z-index:10000; border: 1px solid #b1b1b1; border-top: 2px solid #b1b1b1; line-height:35px; display:inline-block; color:#807a62; font-style:normal; background:url(images/corners2.png) no-repeat right center;}
		.divselectcon input.input1 {border:1px solid #ffae00; border-top: 2px solid #ffae00; margin-left: 0; width: 228px; height:35px;line-height:35px; display:inline-block; color:#807a62; font-style:normal; background:url(images/corners1.png) no-repeat right center;}
		#button2 button {float: right; width: 169px; height: 43px; background: url(images/go.png) 0 0 no-repeat; border: none; position: absolute; top: 9px;  cursor: pointer; }
		.bdsug_copy{display:none;}		
	</style>
	<%-- <link href="${pageContext.request.contextPath}/resource/chain/css/style.default.css" rel="stylesheet"> --%>
	<link href="${pageContext.request.contextPath}/resource/css/manage.common.css" rel="stylesheet">
	<script src="${pageContext.request.contextPath}/resource/chain/js/jquery-1.11.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/chain/js/jquery-migrate-1.2.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/chain/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/chain/js/modernizr.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/chain/js/pace.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/chain/js/retina.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/chain/js/jquery.cookies.js"></script>
	<script src="${pageContext.request.contextPath}/resource/chain/js/custom.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/myform.js" type="text/javascript"></script>
	<script type="text/javascript">
	function topage(page) {
		var form = document.getElementById("pageList");
		/*alert(form);*/
 		$("#inputPage").val(page);
		/*console.log(form);
		form.page.value = page;*/
		form.submit();
	}
		
	</script>
</head>

<body>
<!--上部开始-->
<jsp:include page="../header.jsp"></jsp:include>
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
				<li class="menu-item myOrder on">
					<a href="javascript:;">我的订单</a>
				</li>
				<li class="menu-item cancelOrder">
					<a href="${pageContext.request.contextPath}/user/order/cancellist">已取消的订单</a>
				</li>
				<li class="menu-item myCoupon">
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
					<a href="${pageContext.request.contextPath}/user/information/info">基本信息</a>
				</li>
				<li class="menu-item changePass">
					<a href="${pageContext.request.contextPath}/user/information/updatepassword">修改密码</a>
				</li>
				<li class="menu-item feedBack">
					<a href="${pageContext.request.contextPath}/user/feedback/add">意见反馈</a>
				</li>
			</ul>
		</div>
		<div class="steward1-detail" id="steward1-detail">
		
		
		
			<div id="myOrderContent" class="myOrderContent myOrderDetail" style="border: none; ">
				<ul class="totalTitle">
					<li class="liSelected"><a href="${pageContext.request.contextPath}/user/order/list">所有订单</a></li>
					<li class="li11"><a href="${pageContext.request.contextPath}/user/order/list?orderType=1">洗车订单</a></li>
					<li class=""><a href="${pageContext.request.contextPath}/user/order/list?orderType=2">保养订单</a></li>
					<li class=""><a href="${pageContext.request.contextPath}/user/order/list?orderType=3">商品订单</a></li>
				</ul>
				<form action="${pageContext.request.contextPath}/user/order/list" id="pageList" method="post">
				<div class="orderListsContent">
					<div class="secondTabCon">
						<ul class="titleTop">
							<li class="first">订单</li>
							<li>价格</li>
							<li>数量</li>
							<li>实付金额</li>
							<li>订单状态</li>
							<li>操作</li>
						</ul>
						
						<c:forEach items="${pageView.records}" var="order" >
						<ul class="orderLists">
							<li class="orderTitle">
								<div class="orderTitleLeft">
									<input type="checkbox" /><b><fmt:formatDate value="${order.createDate }" type="date"/></b>
									<p><a href="${pageContext.request.contextPath}/user/order/details?orderId=${order.id }">订单编号：${order.num }</a>
									<c:if test="${order.status==1||order.status==11||order.status==21 }">
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/user/order/cancel?orderId=${order.id}">取消订单</a>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/user/order/enter?orderId=${order.id}">确定订单</a></c:if></p>
								</div>
								
								
											
								<div class="orderTitleRight"><a href="${pageContext.request.contextPath}/user/order/del?id=${order.id}"><img src="${pageContext.request.contextPath}/resource/page/images/del.png" /></a></div>
							</li>
							
							<c:forEach items="${order.serverOrder }" var="serverOrder">
							<li class="orderContent">
								<dl>
									<dt><a href="#"><img src="<%=base%>${serverOrder.serviceType.imgPath}" /></a><b>${serverOrder.serviceType.name }</b></dt>
									<dd>
										<ul>
											<li class="li1">￥${serverOrder.serviceType.price }</li>
											<li class="li2">1</li>
											<li class="li3">￥${serverOrder.order.price }</li>
											<li class="li4">
												<c:if test="${serverOrder.order.status==0 }">已完结</c:if>
												<c:if test="${serverOrder.order.status==1 }">派单中</c:if>
												<c:if test="${serverOrder.order.status==2 }">路途中</c:if>
												<c:if test="${serverOrder.order.status==3 }">服务中</c:if>
												<c:if test="${serverOrder.order.status==11 }">预约成功</c:if>
												<c:if test="${serverOrder.order.status==12 }">已分配车队</c:if>
												<c:if test="${serverOrder.order.status==21 }">已下单</c:if>
												<c:if test="${serverOrder.order.status==22 }">配货中</c:if>
												<c:if test="${serverOrder.order.status==23 }">送货中</c:if>
												
												
												
											</li>
											<%-- <c:if test="${serverOrder.order.status==1||serverOrder.order.status==11||serverOrder.order.status==21 }"><li class="li5"><a href="#"><img src="${pageContext.request.contextPath}/resource/page/images/myOrder5.png" /></a></li>
											<li class="li6" style="clear: both; position: absolute; top: 40px; right: -22px;"><a href="#">确定订单</a></li></c:if> --%>
											
											<c:if test="${serverOrder.comment== null }"><a href="${pageContext.request.contextPath}/user/comment/servercomment?serverOrderId=${serverOrder.id}">评论</a></c:if>
											
											<c:if test="${serverOrder.comment != null }">已评论</c:if>
										</ul>
									</dd>
								</dl>
							</li>
							<c:forEach items="${serverOrder.serverOrderDetail }" var="serverOrderDetail">
								<li class="orderContent">
								<dl>
									<dt><a href="#"><img src="images/myOrder2.png" /></a><b>${serverOrderDetail.product.fullName }</b></dt>
									<dd>
										<ul>
											<li class="li1">￥${serverOrderDetail.product.price }</li>
											<li class="li2">${serverOrderDetail.count }</li>
											<li class="li3">￥${serverOrderDetail.order.price }</li>
											<li class="li4">
												<c:if test="${serverOrderDetail.order.status==0 }">已完结</c:if>
												<c:if test="${serverOrderDetail.order.status==1 }">派单中</c:if>
												<c:if test="${serverOrderDetail.order.status==2 }">路途中</c:if>
												<c:if test="${serverOrderDetail.order.status==3 }">服务中</c:if>
												<c:if test="${serverOrderDetail.order.status==11 }">预约成功</c:if>
												<c:if test="${serverOrderDetail.order.status==12 }">已分配车队</c:if>
												<c:if test="${serverOrderDetail.order.status==21 }">已下单</c:if>
												<c:if test="${serverOrderDetail.order.status==22 }">配货中</c:if>
												<c:if test="${serverOrderDetail.order.status==23 }">送货中</c:if>
												
												</li>
											
											<c:if test="${serverOrderDetail.comment==null}"><a href="${pageContext.request.contextPath}/user/comment/productcomment?serverOrderDetailId=${serverOrderDetail.id}">评论</a></c:if>
											<c:if test="${serverOrderDetail.comment!=null}">已评论</c:if>
										</ul>
									</dd>
								</dl>
							</li>
							</c:forEach>
							</c:forEach>
							
						</ul>
						</c:forEach>
						<c:if test="${pageView.totalrecord > 0}">
						<ul class="comment2 ste">
						<c:if test="${(pageView.currentPage-1)<1}" >
							<li class="pre"><a>&lt;&lt;上一页</a></li>
							</c:if>
							<c:if test="${(pageView.currentPage-1)>=1}" >
							<li class="pre"><a href="javascript:topage(${pageView.currentPage-1})">&lt;&lt;上一页</a></li>
							</c:if>
							<li class="lihover"><a href="javascript:topage(1)">1</a></li>
							<c:forEach begin="${pageView.pageIndex.startindex}" end="${pageView.pageIndex.endindex+1}" var="per">
							<li class="lihover"><a href='javascript:topage(${per})'>${per}</a></li>
							</c:forEach>
							<c:if test="${(pageView.currentPage+1)>pageView.totalPage}" >
							<li class="next"><a>&gt;&gt;下一页</a></li>
							</c:if>
							<c:if test="${(pageView.currentPage+1)<=pageView.totalPage}" >
							<li class="next"><a href="javascript:topage(${pageView.currentPage+1})">&gt;&gt;下一页</a></li>
							</c:if>
							
						</ul>
						</c:if>
						<input type="hidden" value="${pageView.currentPage}" id="inputPage"  maxlength="5" style="width:40px;height: 25px;vertical-align:baseline;" name="page">
					</div>
					<div class="secondTabCon tabContent1" style="display: none; ">
					</div>
					
				</div>
				<%-- <%@ include file="/WEB-INF/page/common/fenye.jsp" %> --%>
				
				</form>
				
				
			</div>
		</div>
	</div>
	<script>
	$(function(){
	function tabs(tabTit,on,active,tabCon){ 
		$(tabCon).each(function(){ 
			$(this).children().eq(0).show();  
		});
		$(tabTit).each(function(){
			//$(this).children().eq(0).addClass(on);
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


/* 	$(function(){
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
	 */
			
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
</div>
</div>
<!--中部结束-->
<!--底部开始-->
<jsp:include page="../footer.jsp"></jsp:include>
<!--底部结束-->
</body>
</html>