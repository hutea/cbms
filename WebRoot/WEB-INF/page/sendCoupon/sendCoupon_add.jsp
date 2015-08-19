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

<title>优惠券发放</title>
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
	
<!-- 公共JS -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resource/js/myform.js"></script>
<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
        <script src="${pageContext.request.contextPath}/resource/chain/js/html5shiv.js"></script>
        <script src="${pageContext.request.contextPath}/resource/chain/js/respond.min.js"></script>
        <![endif]-->
<script type="text/javascript">
	
	function checkNull(v){
		if($(v).val() == ''){
			$("#"+$(v).attr("name")+"_error").html($(v).attr("CHname")+"不能为空");
			$(v).next().val("");
	    } else {
			$("#"+$(v).attr("name")+"_error").html("");
			$(v).next().val("success");
		}
	}
	
	$(document).ready(function(){
		$("#reset").bind("click",function() {
			$("#inputForm")[0].reset();
		});
	});

	function saveType(){
		$(function(){
			checkNull($("#mobile"));
			checkNull($("#num"));
			if($("#mobile").val()!=""){
				checkMember();
			}
			var flag = true;
			$(".repeat").each(function(){
				if($(this).val()!="success"){
					flag = false;
				}
			});
			if(flag){
				$("#inputForm").submit();
			}
		});
	}
	
	function checkMember(){
		var url ="${pageContext.request.contextPath}/manage/sendCoupon/checkMember";
		var data = {
			mobile : $("#mobile").val()
		};
		$.post(url,data,function(result){
			if(result=="false"){
				$("#mobile_error").html("会员不存在！");
				$("#mobile_error").prev().val("");
			}else{
				$("#mobile_error").html("");
				$("#mobile_error").prev().val("success");
			}
		},"text");
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
			$("#type").html(op);
		},"json");
	}
	
	function numChange(){
		var reg =/^[\d]$/;
		var v = $("#num").val();
		if(!reg.test(v) || v<1){
		    $("#num").val("");
		}
	}
	
	$(function(){
		if("${flag}"=="1"){
			alert("发放成功！");
		}
	});
</script>
<STYLE type="text/css">
.form-bordered div.form-group {
	width: 49%;
	padding: 5px 10px;
	border-top: 0px dotted #d3d7db;
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
								<li><a href="">优惠券发放</a></li>
							</ul>
						</div>
					</div>
					<!-- media -->
				</div>
				<!-- pageheader -->

				<div class="contentpanel">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">优惠券发放</h4>
						</div>
						<form class="form-horizontal form-bordered" id="inputForm"
							action="<%=basePath%>/manage/sendCoupon/save" method="POST">
							<div class="panel-body nopadding">

								<div class="form-group">
									<label class="col-sm-4 control-label">用户手机号</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="mobile" maxlength="11" 
										placeholder="请填写用户手机号" id="mobile" CHname="用户手机号" onkeyup="checkMember();">
										<input type="hidden" class="repeat"/>
										<span class="errorStyle" id="mobile_error"></span>
									</div>
								</div>

								<div class="form-group">
									<label class="col-sm-4 control-label">优惠券使用类型</label>
									<div class="col-sm-8">
										<select id="useTypeId" name="useTypeId" class="selects" onchange="useTypeChange(this);">
											<option value="1" selected="selected">洗车优惠券</option>
											<option value="2">保养优惠券</option>
											<option value="3">商品优惠券</option>
										</select>
									</div>
								</div>

								<div class="form-group">
									<label class="col-sm-4 control-label">优惠券类型</label>
									<div class="col-sm-8">
										<select id="type" name="type" class="selects">
											<c:forEach items="${coupons }" var="coupon" >
											<option value="${coupon.id }">${coupon.name }</option>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-sm-4 control-label">优惠券发放数量</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="num" maxlength="10" 
										placeholder="请填写发放数量" id="num" CHname="发放数量" onchange="numChange()">
										<input type="hidden" class="repeat"/>
										<span class="errorStyle" id="num_error"></span>
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
	<script type="text/javascript">
		$('[data-toggle="tooltip"]').popover();
	</script>
</body>
</html>