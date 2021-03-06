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

<!-- 公共JS -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resource/js/myform.js"></script>
<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
        <script src="${pageContext.request.contextPath}/resource/chain/js/html5shiv.js"></script>
        <script src="${pageContext.request.contextPath}/resource/chain/js/respond.min.js"></script>
        <![endif]-->
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
#attributeProductCategory{
	margin-top: 10px;
}
#addAttributeValues{
	position: absolute;
	right: 20px;
	top:15px;
}
.delSpan{
	margin-top: 10px;
	display: inline-block;
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
								<li><a href="">属性添加</a></li>
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
						<form class="form-horizontal form-bordered" id="inputForm" action="<%=basePath%>manage/productAttribute/save" method="POST">
							<div class="panel-body nopadding">
								<div class="form-group">
									<label class="col-sm-4 control-label">绑定分类</label>
									<div class="col-sm-8">
									
										<select id="attributeProductCategory" name="productCategory.id" onchange="changeOption(this);">
											<c:forEach var="category" items="${categorys }">
												<option value="${category.id }" class='<c:if test="${category.attributeSet.size() gt 0 }">choose</c:if>' >
													<c:if test="${category.grade gt 0}">
														<c:forEach begin="1" end="${category.grade }">
															&nbsp;&nbsp;
														</c:forEach>
													</c:if>
													${category.name}
												</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-4 control-label">名称</label>
									<div class="col-sm-8">
										<input type="text" name="name" class="form-control" maxlength="200" value=""/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-4 control-label">排序</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="order" value=""/>
									</div>
								</div>
							</div>
							<div class="panel-heading" style="border-top:1px solid #ddd;">
								<h4 class="panel-title">可选项</h4>
								<button type="button" id="addAttributeValues" onclick="addAttributeValue();">新增可选项</button>								
							</div>
							<div class="panel-body nopadding">
								<div class="table-responsive">
							        <table id="listTable" class="table table-info" >
										<%--<tr class="attribute_value">
											<td>
												<div class="col-sm-8">
													<input type="text" name="attributeValues" class="form-control" maxlength="200" />
												</div>
												<span class="delSpan">
													<a href="javascript:void(0);" onclick="delAttributeValue(this);">[删除]</a>
												</span>
											</td>
										</tr> --%>
									</table>
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
		$(document).ready(function(){
			addAttributeValue();
		});
		function saveForm(){
			
			var productCategory = $("#attributeProductCategory").find("option:selected");
			
			if(productCategory.hasClass("choose")){
				alert("该分类的筛选属性已存在");
				return;
			}
			
			var name = $("input[name='name']").val();
			if(name == ""){
				alert("请填写名称");
				return;
			}
			
			var inputs = $("input[name='attributeValues']");
			if(inputs.length > 0){
				for(var i = 0; i < inputs.length; i++){
					if($(inputs[i]).val() == ""){
						alert("请将可选项填写完整");
						return;
					}
				}
			}else{
				alert("请添加可选项");
				return;
			}
			
			$("#inputForm").submit();
		}
		function addAttributeValue(){
			var html = "<tr class='attribute_value'><td><div class='col-sm-8'>"+
						"<input type='text' name='attributeValues' class='form-control' maxlength='200' />"+
					"</div><span class='delSpan'>"+
						"<a href='javascript:void(0);' onclick='delAttributeValue(this);'>[删除]</a>"+
					"</span></td></tr>";
			$("#listTable").append(html);
		}
		
		function delAttributeValue(e){
			var tr = $(e).closest("tr.attribute_value");
			tr.remove();
		}
		
		function changeOption(e){
			var productCategory = $(e).find("option:selected");
			if(!productCategory.hasClass("choose")){
				alert("该分类的筛选属性已存在");
				return;
			}
		}
	</script>
</body>
</html>
