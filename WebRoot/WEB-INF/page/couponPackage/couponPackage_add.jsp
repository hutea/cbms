<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/page/common/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
<meta name="description" content="">

<title>会员卡管理</title>
<link
	href="${pageContext.request.contextPath}/resource/chain/css/style.default.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resource/chain/css/morris.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resource/chain/css/select2.css"
	rel="stylesheet" />
<link
	href="${pageContext.request.contextPath}/resource/css/manage.common.css"
	rel="stylesheet">

<script
	src="${pageContext.request.contextPath}/resource/chain/js/jquery-1.11.1.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resource/chain/js/jquery-migrate-1.2.1.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resource/chain/js/bootstrap.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resource/chain/js/modernizr.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resource/chain/js/pace.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resource/chain/js/retina.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resource/chain/js/jquery.cookies.js"></script>
<script
	src="${pageContext.request.contextPath}/resource/chain/js/custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/chain/js/laydate/laydate.js"></script>

<!-- 公共JS -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resource/js/myform.js"></script>
<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
        <script src="${pageContext.request.contextPath}/resource/chain/js/html5shiv.js"></script>
        <script src="${pageContext.request.contextPath}/resource/chain/js/respond.min.js"></script>
        <![endif]-->
<script type="text/javascript">
	
	function check(v){
		if($(v).val() == ''){
			$("#"+$(v).attr("name")+"_error").html($(v).attr("CHname")+"不能为空");
			$(v).next().val("fail");
	    } else {
			$("#"+$(v).attr("name")+"_error").html("");
			$(v).next().val("success");
		}
	}
	
	$(document).ready(function(){
		$("#reset").bind("click",function() {
			$("#inputForm")[0].reset();
			couponType();
		});
	});

	function saveType(){
		$(function(){
			check($("#name"));
			check($("#price"));
			check($("#couponCount"));
			check($("#imgPath"));
			var flag = true;
			$(".repeat").each(function(){
				if($(this).prev().is(":visible") && $(this).val()!="success") flag = false;
				if(($(this).prev().attr("id")=="imgPath") && $(this).val()!="success") flag = false;
			});
			if(flag){
				$().ready(function(){
					$("#inputForm").submit();
				});
			}
		});
	}
	
	function priceInputRestrict(e){
		var v=$(e).val();
		var r = /^[0-9]+\.?[0-9]{0,2}$/;
		if(!r.test(v)){
			$(e).val("");
		}
	}
	
	function useTypeChange(e){
		var url ="${pageContext.request.contextPath}/manage/sendCoupon/getCoupon";
		var data = {
			useTypeId : $(e).val()
		};
		$.post(url,data,function(result){
			var op = "";
			for(var i in result){
				op += "<option value="+result[i].id+">"+result[i].name+"</option>";
			}
			$(e).parent().parent().parent().find(".couponName").html(op);
		},"json");
	}
	
	function numChange(e){
		var reg =/^[0-9]*$/;
		var v = $(e).val();
		if(!reg.test(v)){
		    $(e).val("");
		}
	}
</script>
<STYLE type="text/css">
.form-bordered div.form-group {
	width: 49%;
	padding: 5px 10px;
	border-top: 0px dotted #d3d7db;
}
.rdio {
	margin-top: 10px;
	width: 50px;
	
	display: inline-block;
}
.rdio3 {
	width: 30%;
}
.selects {
	padding: 10px;
}
.img_div{
	display: inline-block;
	height: 90px;
	width: 160px;
}
.img_div img{
	display: inline-block;
	height: 90px;
	width: 160px;
}
</STYLE>
</head>
<body>
	<header>
		<%@ include file="/WEB-INF/page/common/head.jsp"%>
	</header>

	<section>
		<div class="mainwrapper">
			<%@ include file="/WEB-INF/page/common/left.jsp"%>

			<div class="mainpanel">
				<div class="pageheader">
					<div class="media">
						<div class="media-body">
							<ul class="breadcrumb">
								<li><a href=""><i class="glyphicon glyphicon-home"></i></a></li>
								<li><a href="">会员卡管理</a></li>
							</ul>
						</div>
					</div>
					<!-- media -->
				</div>
				<!-- pageheader -->

				<div class="contentpanel">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">详细信息</h4>
						</div>
						<form class="form-horizontal form-bordered" id="inputForm"
							action="<%=basePath%>manage/couponPackage/save" method="POST">
							<input type="hidden" name="id" value="${couponPackage.id }">
							<div class="panel-body nopadding">
								<div class="form-group">
									<label class="col-sm-4 control-label">会员卡名称</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="name" CHname="名称" 
											onBlur="check(this)" placeholder="请填写会员卡名称" id="name" value="${couponPackage.name }">
										<input type="hidden" class="repeat"/>
										<span class="errorStyle" id="name_error"></span>
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-sm-4 control-label">会员卡购买价格</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="price" CHname="价格" 
											onBlur="check(this)" onkeyup="priceInputRestrict(this)" placeholder="请填写会员卡价格" id="price" value="${couponPackage.price }">
										<input type="hidden" class="repeat"/>
										<span class="errorStyle" id="price_error"></span>
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-sm-4 control-label">会员卡图片</label>
									<div class="col-sm-8">
										<div class="img_div">
											<img alt="" src="<%=basePath %>${couponPackage.imgPath }" onerror="<%=basePath %>/resource/image/default.png" id="show_img"/>
											<input type="hidden" name="imgPath" id="imgPath" CHname="会员卡图片" value="${couponPackage.imgPath }"/>
											<input type="hidden" class="repeat"/>
											<span class="errorStyle" id="imgPath_error"></span>
										</div>
										<label>
											<span id="spanButtonPlaceholder"></span>
										</label>
									</div>
								</div>

								<div class="form-group">
									<label class="col-sm-4 control-label">会员卡介绍</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="introduction" placeholder="请填写会员卡介绍" maxlength="255" value="${couponPackage.introduction }">
									</div>
								</div>

								<div class="form-group">
									<label class="col-sm-4 control-label">是否启用</label>
									<div class="col-sm-8">
										<div class="rdio rdio-default" style="width: 140px;">
											<input type="radio" name="isEnabled" id="radioZH-One" value="1" <c:if test="${couponPackage.isEnabled eq true || couponPackage.isEnabled eq null}">checked="checked"</c:if>>
											<label for="radioZH-One">是</label>
										</div>
										<div class="rdio rdio-default" style="width: 140px;">
											<input type="radio" name="isEnabled" id="radioZH-two" value="0" <c:if test="${couponPackage.isEnabled eq false}">checked="checked"</c:if>>
											<label for="radioZH-two">否</label>
										</div>
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-sm-4 control-label">优惠券使用类型</label>
									<div class="col-sm-8">
										<select id="useTypeId" name="useTypeId" class="selects" onchange="useTypeChange(this);">
											<option value="1" <c:if test="${couponPackage.coupon.useType eq 1}">selected="selected"</c:if>>洗车优惠券</option>
											<option value="2" <c:if test="${couponPackage.coupon.useType eq 2}">selected="selected"</c:if>>保养优惠券</option>
											<option value="3" <c:if test="${couponPackage.coupon.useType eq 3}">selected="selected"</c:if>>商品优惠券</option>
										</select>
									</div>
								</div>

								<div class="form-group">
									<label class="col-sm-4 control-label">优惠券名称</label>
									<div class="col-sm-8">
										<select id="type" name="coupon.id" class="selects couponName">
											<c:forEach items="${coupons }" var="coupon" >
											<option value="${coupon.id }" <c:if test="${couponPackage.coupon.id eq coupon.id}">selected="selected"</c:if>>${coupon.name }</option>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-sm-4 control-label">优惠券发放数量</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="couponCount" maxlength="10" value="${couponPackage.couponCount }"
										placeholder="请填写数量" id="couponCount" CHname="数量" onchange="numChange(this)">
										<input type="hidden" class="repeat"/>
										<span class="errorStyle" id="couponCount_error"></span>
									</div>
								</div>
								
							</div>
						</form>
						<div class="panel-footer">
							<div class="row">
								<div class="col-sm-9 col-sm-offset-3">
									<button id="addCate" class="btn btn-primary mr5" onclick="saveType()">提交</button>
									<button id="reset" class="btn btn-dark" type="reset">重置</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="bottomwrapper">
					<%@ include file="/WEB-INF/page/common/bottom.jsp"%>
				</div>
			</div>
			<!-- mainpanel -->
		</div>
		<!-- mainwrapper -->
	</section>
	<!-- 上传图片页面 -->
	<jsp:include page="../common/imgUpload.jsp"></jsp:include>
	
	<script type="text/javascript">
		$('[data-toggle="tooltip"]').popover();
	</script>
</body>
</html>
