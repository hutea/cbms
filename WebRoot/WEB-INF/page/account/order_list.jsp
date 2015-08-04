<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/page/common/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String base = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
	<meta name="description" content="">
	<title>Chain Responsive Bootstrap3 Admin</title>
	<link href="${pageContext.request.contextPath}/resource/chain/css/style.default.css" rel="stylesheet">
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
	<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
	<script src="js/html5shiv.js"></script>
	<script src="js/respond.min.js"></script>
	<![endif]-->
	<script type="text/javascript">
	  	function del(accid){
		if (confirm('您确定要禁用吗')) {
			var url = "${pageContext.request.contextPath}/manage/account/delete";
			var data = {ids:accid};
			$.get(url,data,function(data) {
		      	if(data.status=="success"){
		      		$("#td_"+accid).html("禁用");
		       	}
			   },"json");
			}
		}
	</script>
	<style type="text/css">
		#chooseTeamContent label{
			margin-right: 15px;
		}
	</style>
</head>

<body>
	<%@ include file="/WEB-INF/page/common/head.jsp" %>
    <section>
    <div class="mainwrapper">
		<%@ include file="/WEB-INF/page/common/left.jsp" %>
         <div class="mainpanel">
		    <div class="pageheader">
		      <div class="media">
		        <div class="media-body">
		          <ul class="breadcrumb">
		            <li><a href=""><i class="glyphicon glyphicon-home"></i></a></li>
		            <li><a href="">地区列表</a></li>
		          </ul>
		        </div>
		      </div><!-- media -->
		    </div>
		    <form id="pageList" action="${pageContext.request.contextPath}/manage/order/list" method="post">
		    	<input type="hidden" name="searchProp" value="${searchProp }"/>
			    <div class="contentpanel">
			      <div class="search-header">
			        <div class="btn-list">
			          <button class="btn btn-danger" id="deleteButton" type="button" val="<%=path %>/manage/order" disabled>删除</button>
			          <button class="btn btn-success" id="refreshButton">刷新</button>
					 <%--  <button id="add" type="button" class="btn btn-primary" val="<%=path %>/manage/order">添加</button> --%>
			          <div style="float: right;max-width: 340px;height: 37px;">
			            <div class="input-group" style="float: left;max-width: 240px;">
			              <input id="searchValue" placeholder="关键字查询" name="queryContent" value="${queryContent}" type="text" class="form-control" maxlength="50" style="height: 37px">
			            </div>
			            <div style="float: right">
			            	<button type="button" class="btn btn-info btn-metro" onclick="confirmQuery();">查询</button>
			            </div>
			          </div>
			        </div>
			      </div>
			
			      <div class="table-responsive">
			        <table id="listTable" class="table table-info" >
			        	<thead>
							<tr>
								<th class="check">
									<input type="checkbox" id="selectAll"/>
								</th>
								<th>
									收货人
								</th>
								<th>
									订单金额
								</th>
								<th>
									服务
								</th>
								<th>
									地区
								</th>
								<th>
									预约时间
								</th>
								<th>
									状态
								</th>
								<th>
									跟进车队
								</th>
								<th>
									创建时间
								</th>
								<th>
									操作
								</th>
							</tr>
						</thead>
						<c:forEach items="${pageView.records}" var="order">  
							<tr>
								<td>
									<input type="checkbox" name="ids" value="${order.id}" />
								</td>
								<td>
									${order.contact }
								</td>
								<td>
									${order.price }
								</td>
								<td>
									${order.serviceType.name }
								</td>
								<td>
									${order.area.treeFull }
								</td>
								<td>
									${order.dateTimeMap }
								</td>
								<td>
									${order.statusString }
								</td>
								<td>
									${order.carTeam.headMember}
								</td>
								<td>
									<span><fmt:formatDate value="${order.createDate}" pattern="yyyy-MM-dd"/></span>
								</td>
								<td><!-- data-toggle="modal" data-target=".bs-example-modal-lg" -->
									<c:if test="${order.status eq 1}">
										<a href="javascript:void(0);" onclick="showModal('${order.id}');">[服务]</a>
									</c:if>
									<c:if test="${order.status eq 2}">
										<a href="javascript:void(0);" onclick="endOrder('${order.id}');">[完结]</a>
									</c:if>
									<a href="javascript:void(0);" onclick="errorOrder('${order.id}');">[失败]</a>
								</td>
							</tr>
						  </c:forEach>
					</table>
		            <%@ include file="/WEB-INF/page/common/fenye.jsp" %>
			      </div>
			    </div>
		   	 </form>
		  </div>       
    </div> <!-- mainwrapper -->
    </section>
   
   <div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-hidden="true" style="display: none;">
        <div class="modal-dialog modal-lg">
          <div class="modal-content">
              <div class="modal-header">
                  <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                  <h4 class="modal-title">选择服务车队</h4>
              </div>
              <input type="hidden" value="" id="chooseOrderId"/>
              <div class="modal-body" id="chooseTeamContent">
              		<label><input type="radio" name="carTeam" value=""/></label>
              </div>
              <div class="modal-footer">
             		<button data-dismiss="modal" type="button" aria-hidden="true" class="btn btn-dark">关闭</button>
              		<button type="button" onclick="chooseTeam();" class="btn btn-primary mr5">确定</button>
              </div>
          </div>
        </div>
    </div>
   <script type="text/javascript">
   		var base = "<%=base %>";
   		function showModal(obj){
   			$("#chooseOrderId").val(obj);
   			var url = base + "manage/order/getCarTeam";
   			var data = {orderId:obj};
   			$.post(url,data,function(result){
   				if(result.status == "success"){
   					addLabel(result.message);
   				}else{
   					addError();
   				}
   			},"json");
   			
   			$(".bs-example-modal-lg").modal();
   		}
   		
   		function addLabel(value){
			var label = "";
			for(var i in value){
				label += "<label><input type='radio' name='carTeam' value='"+value[i].id+"'/>"+value[i].name+"</label>";
			}
			$("#chooseTeamContent").html(label);
   		}
   		function addError(){
   			$("#chooseTeamContent").html("暂无可以分配的车队");
   		}
   		//选择车队
   		function chooseTeam(){
   			var carTeam = $("input[name='carTeam']:checked");
   			var carTeamId = "";
   			if(carTeam.length == 1){
   				carTeamId = $(carTeam[0]).val();
   			}
   			if(carTeamId == ""){
   				alert("请选择车队");
   				return;
   			}
   			var orderId = $("#chooseOrderId").val();
   			var url = base + "manage/order/updateOrderToTeam";
   			var data = {
   					teamId:carTeamId,
   					orderId:orderId
   			};
   			$.post(url,data,function(result){
   				if(result.status == "success"){
   					window.location.reload(true);
   				}else{
   					alert("选择车队失败");
   				}
   			},"json");
   		}
   		
   		//完结订单
   		function endOrder(obj){
   		
   			var url = base + "manage/order/endOrder";
   			var data = {
   					orderId:obj
   			};
   			$.post(url,data,function(result){
   				if(result.status == "success"){
   					window.location.reload(true);
   				}
   			},"json");
   		}
   		
   		//失败订单
   		function errorOrder(obj){
   			var url = base + "manage/order/errorOrder";
   			var data = {
   					orderId:obj
   			};
   			$.post(url,data,function(result){
   				if(result.status == "success"){
   					window.location.reload(true);
   				}
   			},"json");
   		}
   </script>
</body>
</html>
