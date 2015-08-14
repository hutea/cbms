<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/page/common/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<c:forEach var="value" items="${parameters}">
	<div class="form-group">
		<input type="hidden" name="parameterIds" value="${value.id }"/>
		<label class="col-sm-4 control-label">${value.name }</label>
		<div class="col-sm-8">
			<input type="text" name="parameterValues" class="form-control" maxlength="200" id="${value.id }"/>
		</div>
	</div>
</c:forEach>
<script type="text/javascript">
	var array = '${productPara}';
	console.log(array);
	$(document).ready(function(){
		setParameterValue();
	});
	function setParameterValue(){
		if(array != ""){
			var ar = eval("("+array+")");
			for(var i in ar){
				var data = ar[i];
				$("#"+data.id).val(data.value);
			}
		}
	}
</script>

