<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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

<title>一动车保首页</title>
<link href="<%=basePath%>resource/page/css/style.css" type="text/css"
	rel="stylesheet" />
<script type="text/javascript"
	src="<%=basePath%>resource/page/js/jquery.js"></script>
<script type="text/javascript"
	src="<%=basePath%>resource/page/js/function.js"></script>
<style type="text/css">
/******form*******/
.divselectcon {
	clear: both;
	padding: 10px 0 20px;
	position: relative;
}

.divselect {
	float: left;
	margin-left: -20px;
}

.divselectcon input {
	width: 242px;
	height: 35px;
	margin: 0 20px;
	position: relative;
	z-index: 10000;
	border: 1px solid #b1b1b1;
	border-top: 2px solid #b1b1b1;
	line-height: 35px;
	display: inline-block;
	color: #807a62;
	font-style: normal;
	background: url(<%=basePath%>resource/page/images/corners2.png)
		no-repeat right center;
	padding-left:5px;
}

.divselectcon input.input1 {
	border: 1px solid #ffae00;
	border-top: 2px solid #ffae00;
	/*margin-left: 0;*/
	width: 242px;
	height: 35px;
	line-height: 35px;
	display: inline-block;
	color: #807a62;
	font-style: normal;
	background: url(<%=basePath%>resource/page/images/corners1.png)
		no-repeat right center;
}

#button2 button {
	float: right;
	width: 169px;
	height: 43px;
	background: url(<%=basePath%>resource/page/images/baoyang.png) 0 0 no-repeat;
	border: none;
	position: absolute;
	top: 9px;
	cursor: pointer;
}

.bdsug_copy {
	display: none;
}

.top_right li.first a {
	color: #ffae00;
}

.top_right li b.b1 {
	background: url(<%=basePath%>resource/page/images/1.png) 0 0 no-repeat;
}

.input_search_result {
	position: absolute;
	height: 125px;
	margin-left: 20px;
	border: 1px solid #ddd;
	overflow-y: auto;
	width: 25%;
	background-color: white;
	display: none;
}

.input_search_result ul li {
	cursor: pointer;
}

.input_search_result ul li:HOVER {
	background-color: beige;
}
</style>

</head>

<body>
	<!--上部开始-->
	<div class="top">
		<%-- <%@ include file="/WEB-INF/page/web/header.jsp" %> --%>
		<jsp:include page="../web/header.jsp" />
		<!--box banner start-->
		<!--box banner end-->
	</div>
	<!--上部结束-->
	<!--中部开始-->
	<div class="mid box1" style="height: auto;">
		<div class="span1" style="height: 225px;">
			<div class="sel_title">
				<h2>请选择您的车型</h2>
				<b class="b4"></b>
			</div>
			<ul class="ul0">
				<li class="hot">热门搜索:</li>
				<li><a href="#">别克</a></li>
				<li><a href="#">本田</a></li>
				<li><a href="#">大众</a></li>
				<li><a href="#">丰田</a></li>
				<li><a href="#">福特</a></li>
				<li><a href="#">起亚</a></li>
				<li><a href="#">现代</a></li>
				<li><a href="#">雪铁龙</a></li>
			</ul>
			<script type="text/javascript">
			function getValue(e){
				var keyWord = $(e).val();
				if($(e).hasClass("carBrand")){
					getCarBrand(keyWord);
				}else if($(e).hasClass("carType")){
					getCarType(null,$("#carBrandId").val(),keyWord);
				}
			}
			//获取品牌
			function getCarBrand(keyWord){
				var url ="${pageContext.request.contextPath}/web/car/getCarBrand";
				var data = {
					keyWord : keyWord
				};
				$.post(url,data,function(result){
					var li = "";
					for(var i in result){
						li += "<li onclick='getCarType(this,\""+result[i].id+"\",null)'>"+result[i].name+"</li>";
					}
					$("#carBrand").html(li);
					$("#carBrand").parent().show();
				},"json");
			}
			//获取车系
			function getCarType(e,carBrandId,keyWord){
				if(e!=null){
					$("#carBrandInput").val($(e).html());
				}
				$("#carBrandId").val(carBrandId);
				var url ="${pageContext.request.contextPath}/web/car/getCarType";
				var data = {
					carBrandId : carBrandId,
					keyWord : keyWord
				};
				$.post(url,data,function(result){
					var li = "";
					for(var i in result){
						li += "<li onclick='getCar(this,\""+result[i].id+"\")'>"+result[i].name+"</li>";
					}
					$("#carType").html(li);
					$("#carBrand").parent().hide();
					$("#carType").parent().show();
				},"json");
			}
			//获取车型
			function getCar(e,carTypeId){
				if(e!=null){
					$("#carTypeInput").val($(e).html());
				}
				$("#carTypeId").val(carTypeId);
				var url ="${pageContext.request.contextPath}/web/car/getCar";
				var data = {
					carTypeId : carTypeId
				};
				$.post(url,data,function(result){
					var li = "";
					for(var i in result){
						li += "<li onclick='getCarId(this,\""+result[i].id+"\")'>"+result[i].name+"</li>";
					}
					$("#car").html(li);
					$("#carType").parent().hide();
					$("#car").parent().show();
				},"json");
			}
			function getCarId(e,carId){
				$("#carInput").val($(e).html());
				$("#carId").val(carId);
				$(e).parent().parent().hide();
			}
			function inputFocus(e){
				$(".carSelectInput").removeClass("input1");
				$(e).addClass("input1");
				$(".input_search_result").hide();
				if($(e).next().children().children().size() != 0){
					$(e).next().show();
				}
				if($(e).hasClass("carBrand")){
					getCarBrand(null);
				}
			}
			function carSubmit(){
				var h = "${pageContext.request.contextPath}/web/gotoService?carId="+$("#carId").val();
				location.href = h;
			}
		</script>
			<div class="divselectcon">
				<div class="divselect">
					<div style="width: 32%;display: inline-block;">
						<input type="text" placeholder="选择品牌" class="carSelectInput carBrand"
							id="carBrandInput" onkeyup="getValue(this);" onclick="inputFocus(this)" />
						<div class="input_search_result">
							<ul id="carBrand"></ul>
						</div>
						<input type="hidden" id="carBrandId"/>
					</div>
					<div style="width: 32%;display: inline-block;">
						<input type="text" placeholder="选择车系" class="carSelectInput carType"
						id="carTypeInput" onkeyup="getValue(this);" onclick="inputFocus(this)" />
						<div class="input_search_result">
							<ul id="carType"></ul>
						</div>
						<input type="hidden" id="carTypeId"/>
					</div>
					<div style="width: 32%;display: inline-block;">
						<input type="text" placeholder="选择车型" class="carSelectInput car" 
						id="carInput" readonly="readonly" onclick="inputFocus(this)" />
						<div class="input_search_result">
							<ul id="car"></ul>
						</div>
						<input type="hidden" id="carId"/>
					</div>
				</div>

				<div id="button2">
					<button class="button" onclick="carSubmit()"></button>
				</div>
			</div>
		</div>
		<div class="span2" style="height: 200px;">
			<div class="charact_left">
				<dl class="icon1">
					<dt>
						<a href="#">创新</a>
					</dt>
					<dd>保养时间地点由您做主&nbsp;&nbsp;智能保养爱车养护无忧&nbsp;&nbsp;平均价格比4S店低40%</dd>
				</dl>
				<dl class="icon2">
					<dt>
						<a href="#">正品</a>
					</dt>
					<dd>原厂品质&nbsp;&nbsp;正品配件深厚的产业背景和厂商资源&nbsp;&nbsp;严格控制进货渠道&nbsp;&nbsp;杜绝一切假冒伪劣配件</dd>
				</dl>
			</div>
			<div class="charact_right">
				<dl class="icon3">
					<dt>
						<a href="#">专业</a>
					</dt>
					<dd>十年经验专业汽车团队&nbsp;&nbsp;一动车保五级质量保证体系&nbsp;&nbsp;阶梯式保养顾问服务</dd>
				</dl>
				<dl class="icon4">
					<dt>
						<a href="#">便捷</a>
					</dt>
					<dd>标准化流程省时省心&nbsp;&nbsp;随时随地在线预约&nbsp;&nbsp;微信预约&nbsp;&nbsp;电话预约&nbsp;&nbsp;下单&nbsp;&nbsp;上门提供服务</dd>
				</dl>
			</div>
		</div>
	</div>
	<!--中部结束-->
	<!--底部开始-->
	<div class="down">
		<div class="span4">
			<img class="span41"
				src="<%=basePath%>resource/page/images/down_img.png" alt="" />
			<div class="span42">
				<h2>全天24小时洗车</h2>
				<p>一动车保的洗车师傅数量最多，合作门店数量最多，提供给车主的选择最多。全面支持微信公众号及APP客户端注册，动几下手指，即可轻松搞定预约、查询、支付等环节。一动车保统一行业标准，设置高标准服务流程，并对所有洗车师傅和合作门店进行背景调查和面试，确保车主全程无忧。充值返利常态化，一动车保以超低的价格为车主提供洗车服务。</p>
			</div>
		</div>
		<jsp:include page="../web/footer.jsp" />
	</div>
	<!--底部结束-->
</body>
</html>