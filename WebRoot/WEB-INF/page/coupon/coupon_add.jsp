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

<title>优惠券添加</title>
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
		if($(v).is(':visible')){
			if($(v).val() == ''){
				$("#"+$(v).attr("name")+"_error").html($(v).attr("CHname")+"不能为空");
				$(v).next().val("");
		    } else {
				$("#"+$(v).attr("name")+"_error").html("");
				$(v).next().val("success");
			}
		}
	}
	$(document).ready(function(){
		$("#reset").bind("click",function() {
			$("#repeat").val("");
			$("#inputForm")[0].reset();
			couponType();
// 			isExchange();
		});
	});
	$(document).ready(function(){
		couponType();
// 		isExchange();
	});
	
	function isVisible(e){
		if($(e).is(":visible")){
			check($(e));
		}
	}

	function saveType(){
		$(function(){
			isVisible($("#name"));
// 			isVisible($("#point"));
			isVisible($("#discount"));
			isVisible($("#minPrice"));
			isVisible($("#rate"));
			var flag = true;
			$(".repeat").each(function(){
				if($(this).prev().is(":visible") && $(this).val()!="success") flag = false;
			});
			if(flag){
				$().ready(function(){
					couponTypeRemoveAttr();
					$("#inputForm").submit();
				});
			}
		});
	}
	
	function couponType(){
		if($("input[name=type]:checked").val()==1){
			$("#discountDiv").hide();
			$("#minPriceDiv").show();
			$("#rateDiv").show();
		}else if($("input[name=type]:checked").val()==2){
			$("#discountDiv").show();
			$("#minPriceDiv").show();
			$("#rateDiv").hide();
		}else if($("input[name=type]:checked").val()==3){
			$("#discountDiv").show();
			$("#minPriceDiv").hide();
			$("#rateDiv").hide();
		}
	}
	
	function couponTypeRemoveAttr(){
		if($("input[name=type]:checked").val()==1){
			$("#discount").removeAttr("value");
		}else if($("input[name=type]:checked").val()==2){
			$("#rate").removeAttr("value");
		}else if($("input[name=type]:checked").val()==3){
			$("#minPrice").removeAttr("value");
			$("#rate").removeAttr("value");
		}
	}
	
// 	function isExchange(){
// 		if($("input[name=isExchange]:checked").val()==1){
// 			$("#pointDiv").show();
// 		}else{
// 			$("#pointDiv").hide();
// 		}
// 	}
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
.img_div{
	display: inline-block;
	height: 60px;
	width: 160px;
}
.img_div img{
	display: inline-block;
	height: 60px;
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
								<li><a href="">优惠券添加</a></li>
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
							action="<%=basePath%>manage/coupon/save" method="POST">
							<div class="panel-body nopadding">
								<div class="form-group">
									<label class="col-sm-4 control-label">优惠券名称</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="name" CHname="名字" 
											onBlur="check(this)" placeholder="请填写优惠券名称" id="name">
										<input type="hidden" class="repeat"/>
										<span class="errorStyle" id="name_error"></span>
									</div>
								</div>

								<div class="form-group">
									<label class="col-sm-4 control-label">使用起始时间</label>
									<div class="col-sm-8">
										<div class="input-group">
											<input type="text" class="form-control hasDatepicker" placeholder="不填代表从现在开始" id="beginDate" name="beginDate" onclick="getLaydate('beginDate')">
											<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
										</div>
									</div>
								</div>

								<div class="form-group">
									<label class="col-sm-4 control-label">使用结束时间</label>
									<div class="col-sm-8">
										<div class="input-group">
											<input type="text" class="form-control hasDatepicker" placeholder="不填代表永久有效" id="endDate" name="endDate" onclick="getLaydate('endDate')">
											<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
										</div>
									</div>
								</div>

								<div class="form-group">
									<label class="col-sm-4 control-label">优惠券介绍</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="introduction" placeholder="请填写优惠券介绍" maxlength="255">
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-sm-4 control-label">优惠券图片</label>
									<div class="col-sm-8">
										<div class="img_div">
											<img alt="" src="" onerror="<%=basePath %>/resource/image/default.png" id="show_img"/>
											<input type="hidden" name="imgPath" value=""/>
										</div>
										<label>
											<span id="spanButtonPlaceholder"></span>
										</label>
										
									</div>
								</div>

								<div class="form-group">
									<label class="col-sm-4 control-label">是否启用</label>
									<div class="col-sm-8">
										<div class="rdio rdio-default" style="width: 140px;">
											<input type="radio" name="isEnabled" id="radioZH-One" value="1" checked="checked">
											<label for="radioZH-One">是</label>
										</div>
										<div class="rdio rdio-default" style="width: 140px;">
											<input type="radio" name="isEnabled" id="radioZH-two" value="0">
											<label for="radioZH-two">否</label>
										</div>
									</div>
								</div>
								
								<!-- <div class="form-group">
									<label class="col-sm-4 control-label">是否允许积分兑换</label>
									<div class="col-sm-8" onchange="isExchange()">
										<div class="rdio rdio-default" style="width: 140px;">
											<input type="radio" name="isExchange" id="radioZH-3" value="1">
											<label for="radioZH-3">是</label>
										</div>
										<div class="rdio rdio-default" style="width: 140px;">
											<input type="radio" name="isExchange" id="radioZH-4" value="0" checked="checked">
											<label for="radioZH-4">否</label>
										</div>
									</div>
								</div> -->

								<!-- <div class="form-group" id="pointDiv">
									<label class="col-sm-4 control-label">兑换所需积分</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="point" id="point" placeholder="请填写积分数" maxlength="11" CHname="积分" onBlur="check(this)">
										<input type="hidden" class="repeat"/>
										<span class="errorStyle" id="point_error"></span>
									</div>
								</div> -->
								
								<div class="form-group">
									<label class="col-sm-4 control-label">优惠券类型</label>
									<div class="col-sm-8 rdio3" onchange="couponType()">
										<div class="rdio rdio-default" style="width: 140px;">
											<input type="radio" name="type" id="radioZH-5" value="1" checked="checked">
											<label for="radioZH-5">满额打折</label>
										</div>
										<div class="rdio rdio-default" style="width: 140px;">
											<input type="radio" name="type" id="radioZH-6" value="2">
											<label for="radioZH-6">满额优惠</label>
										</div>
										<div class="rdio rdio-default" style="width: 140px;">
											<input type="radio" name="type" id="radioZH-7" value="3">
											<label for="radioZH-7">减免</label>
										</div>
									</div>
								</div>

								<div class="form-group" id="discountDiv">
									<label class="col-sm-4 control-label">折扣额</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="discount" id="discount" placeholder="请填写折扣额" CHname="折扣额" onBlur="check(this)">
										<input type="hidden" class="repeat"/>
										<span class="errorStyle" id="discount_error"></span>
									</div>
								</div>

								<div class="form-group" id="rateDiv">
									<label class="col-sm-4 control-label">折扣率（折）</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="rate" id="rate" placeholder="请填写折扣率" maxlength="1" CHname="折扣率" onBlur="check(this)">
										<input type="hidden" class="repeat"/>
										<span class="errorStyle" id="rate_error"></span>
									</div>
								</div>

								<div class="form-group" id="minPriceDiv">
									<label class="col-sm-4 control-label">最小商品价格</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="minPrice" id="minPrice" placeholder="请填写最小商品价格" CHname="价格" onBlur="check(this)">
										<input type="hidden" class="repeat"/>
										<span class="errorStyle" id="minPrice_error"></span>
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
		
		//设置时间
		function getLaydate(obj){
			var option = {
				elem : '#'+obj,
				format : 'YYYY-MM-DD hh:mm:ss',
				istime : true,
				istoday : false
			};
			laydate(option);
		}
	</script>
</body>
</html>
