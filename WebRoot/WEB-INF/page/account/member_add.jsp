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

<title></title>
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
<script
	src="${pageContext.request.contextPath}/resource/chain/js/select2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/chain/js/laydate/laydate.js"></script>

	
<!-- 验证框架 -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resource/js/jquery.validate.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resource/js/jquery.maskedinput-1.0.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resource/js/validate/account.js"></script>

<!-- 公共JS -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resource/js/myform.js"></script>
<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
        <script src="${pageContext.request.contextPath}/resource/chain/js/html5shiv.js"></script>
        <script src="${pageContext.request.contextPath}/resource/chain/js/respond.min.js"></script>
        <![endif]-->
	
<script type="text/javascript">
	function checkUsername() {
		var username = $("#username").val();
		$.post("${pageContext.request.contextPath}/manage/serviceType/checkUsername.action",
		{
			username : username
		},
		function(data) {
			if (data == 0 && username != "" && username != null) {//表示 帐户存在
				$("#username_error").html("用户名已经存在");
				$("#repeat").val("");
			} else {
				$("#username_error").html("");
				$("#repeat").val("success");
			}
		});
	}
</script>
<STYLE type="text/css">
.form-bordered div.form-group {
	width: 49%;
	padding: 5px 10px;
	border-top: 0px dotted #d3d7db;
}

.img_div{
	display: inline-block;
	height: 60px;
	width: 60px;
}
.img_div img{
	display: inline-block;
	height: 60px;
	width: 60px;
}
.rdio {
	margin-top: 10px;
	width: 50px;
	display: inline-block;
}
#area_div_select select{
	margin-top: 10px;
}
#memberRankSelect{
	margin-top: 10px;
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
								<li><a href="">会员添加</a></li>
							</ul>
						</div>
					</div>
					<!-- media -->
				</div>
				<!-- pageheader -->

				<div class="contentpanel">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">基本信息</h4>
						</div>
						<form class="form-horizontal form-bordered" id="inputForm"
							action="<%=basePath%>manage/member/save" method="POST">
							<input type="hidden" name="id" value="${entity.id }"/>
							<div class="panel-body nopadding">
								<div class="form-group">
									<label class="col-sm-4 control-label">邮箱</label>
									<div class="col-sm-8">
										<input type="text" name="email" class="form-control" maxlength="200" value=""/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-4 control-label">手机号码</label>
									<div class="col-sm-8">
										<input type="text" name="mobile" class="form-control" maxlength="200" value=""/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-4 control-label">密码</label>
									<div class="col-sm-8">
										<input type="password" name="password" class="form-control" maxlength="200"/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-4 control-label">重复密码</label>
									<div class="col-sm-8">
										<input type="password" class="form-control" maxlength="200" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-4 control-label">积分</label>
									<div class="col-sm-8">
										<input type="text" name="amount" class="form-control"/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-4 control-label">余额</label>
									<div class="col-sm-8">
										<input type="text" name="money" class="form-control"/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-4 control-label">会员等级</label>
									<div class="col-sm-8">
										<select id="memberRankSelect" name="memberRank.id">
											<option value="">请选择</option>
											<c:forEach var="memberRank" items="${memberRanks }">
												<option value="${membeRank.id }">${memberRank.name }</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-4 control-label">设置</label>
									<div class="col-sm-8">
										<div class="rdio rdio-default" style="width: 140px;">
											<input type="radio" name="status" id="status-two" value="1" checked="checked"
												><label for="status-two">启用</label>
										</div>
										<div class="rdio rdio-default" style="width: 140px;">
											<input type="radio" name="status" id="status-One" value="0"><label
												for="status-One">停用</label>
										</div>
									</div>
								</div>
							</div>
							<div class="panel-heading" style="border-top: 1px solid #ddd;">
								<h4 class="panel-title">详细信息</h4>
							</div>
							<div class="panel-body nopadding">
								<div class="form-group">
									<label class="col-sm-4 control-label">用户姓名</label>
									<div class="col-sm-8">
										<input type="text" name="name" class="form-control" maxlength="20"/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-4 control-label">性别</label>
									<div class="col-sm-8">
										<div class="rdio rdio-default" style="width: 140px;">
											<input type="radio" name="gender" id="radioZH-One" value="0" checked="checked"><label
												for="radioZH-One">男</label>
										</div>
										<div class="rdio rdio-default" style="width: 140px;">
											<input type="radio" name="gender" id="radioZH-two" value="1"
												><label for="radioZH-two">女</label>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-4 control-label">出生日期</label>
									<div class="col-sm-8">
										<div class="input-group">
											<input type="text" class="form-control hasDatepicker" placeholder="" id="reportDay" name="birth" onclick="getLaydate('reportDay')">
											<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-4 control-label">地区</label>
									<div class="col-sm-8" id="area_div_select">
										<select onchange="getAreaList(this);">
											<option value=''>请选择</option>
											<c:forEach var="area" items="${areas }">
												<option value="${area.id }" <c:if test="${area.children.size()>0 }">class="hasNext"</c:if>
												>${area.name }</option>
											</c:forEach>
										</select>
									</div>
									<input type="hidden" id="areaSelectId" name="area.id"/>
								</div>
								<div class="form-group">
									<label class="col-sm-4 control-label">地址</label>
									<div class="col-sm-8">
										<input type="text" name="address" class="form-control"/>
									</div>
								</div>
							</div>
							<div class="panel-footer">
								<div class="row">
									<div class="col-sm-9 col-sm-offset-3">
										<button id="addCate" class="btn btn-primary mr5"
											onclick="saveForm()" type="button">提交</button>
										<button class="btn btn-dark" type="reset">重置</button>
									</div>
								</div>
							</div>
						</form>
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
		var base = "<%=basePath%>";
		$('[data-toggle="tooltip"]').popover();
		$(document).ready(function(){
			$("#serviceType").select2({
				minimumResultsForSearch : -1,
				allClear : true
			});
		});
		function saveForm(){
			$("#inputForm").submit();
		}
		//设置时间
		function getLaydate(obj){
			var option = {
				elem : '#'+obj,
				format : 'YYYY-MM-DD',
				istime : true,
				istoday : false
			};
			laydate(option);
		}
		//选择地区
		function getAreaList(e){
			delSelect(e);
			var option = $(e).find("option:selected");
			var id = option.val();
			$("#areaSelectId").val(id);
			if(!option.hasClass("hasNext")){
				return;
			}
			var url = base + "/manage/area/findArea";
			$.post(url,{id:id},function(result){
				if(result.status == "success"){
					var values = result.message;
					addSelectElement(values);
				}
			},"json");
		}
		
		//设置地区
		function delSelect(e){
			$(e).nextAll().remove();
		}
		
		//添加选择元素
		function addSelectElement(value){
			var options = "<option value=''>请选择</option>";
			for(var i in value){
				options += "<option value='"+value[i].id+"' class='"+value[i].hasNext+"'>"+value[i].text+"</option>";
			}
			var select = "<select onchange='getAreaList(this);'>"+options+"</select>";
			$("#area_div_select").append(select);
			
		}
	</script>
</body>
</html>
