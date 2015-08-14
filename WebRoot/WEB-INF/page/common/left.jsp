<%@ page language="java" pageEncoding="UTF-8"%>
<div class="leftpanel">
	<div class="media profile-left">
		<a class="pull-left profile-thumb" 	href='' >
			<img class="img-circle" src="${pageContext.request.contextPath}/resource/chain/images/photos/profile.png" alt=""> 
		</a>
		<div class="media-body">
			<h4 class="media-heading">${loginAccount.nickname}</h4>
		</div>
	</div>
	<!-- media -->

	<h3 class="leftpanel-title">
		菜单
	</h3>
	<ul class="nav nav-pills nav-stacked">
		<li <c:if test="${m==null}">class="active"</c:if> >
			<a href=""><i class="fa fa-home"></i> <span>首页</span>
			</a>
		</li>
		<li class="<c:if test="${param.m==1||m==1}">active</c:if> parent" >
			<a href=""><i class="fa fa-bars"></i><span>商品管理</span></a>
			<ul class="children">
				<li><a href='${pageContext.request.contextPath}/manage/product/list'>商品管理</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/productCategory/list'>商品分类</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/productAttribute/list'>商品筛选属性</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/parameterGroup/list'>商品参数</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/productBrand/list'>商品品牌</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/specification/list'>商品规格</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/productLabel/list'>商品标签</a></li>
			</ul>
		</li>
		<li class="<c:if test="${param.m==2||m==2}">active</c:if> parent" >
			<a href=""><i class="fa fa-bars"></i><span>车型管理</span></a>
			<ul class="children">
				<li><a href='${pageContext.request.contextPath}/manage/carBrand/list'>品牌管理</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/carType/list'>车系管理</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/car/list'>车型管理</a></li>
			</ul>
		</li>
		<li class="<c:if test="${param.m==4||m==4}">active</c:if> parent" >
			<a href=""><i class="fa fa-bars"></i><span>服务管理</span></a>
			<ul class="children">
				<li><a href='${pageContext.request.contextPath}/manage/serviceType/list'>服务列表</a></li>
				<%-- <li><a href='${pageContext.request.contextPath}/manage/serviceStore/list'>服务门店</a></li> --%>
			</ul>
		</li>
		<li class="<c:if test="${param.m==3||m==3}">active</c:if> parent" >
			<a href=""><i class="fa fa-bars"></i><span>帐号管理</span></a>
			<ul class="children">
				<li><a href='${pageContext.request.contextPath}/manage/account/list'>系统帐号管理</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/account/group/list'>角色定义</a></li>
				
			</ul>
		</li>
		<li class="<c:if test="${param.m==5||m==5}">active</c:if> parent" >
			<a href=""><i class="fa fa-bars"></i><span>订单管理</span></a>
			<ul class="children">
				<li><a href='${pageContext.request.contextPath}/manage/order/list'>订单管理</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/order/list?endOrder=true'>已完结订单</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/order/market_list'>市场部带审核列表</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/order/finance_list'>财务部带审核列表</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/order/cancel_list'>退款成功列表</a></li>
			</ul>
		</li>
		
		<li class="<c:if test="${param.m==6||m==6}">active</c:if> parent" >
			<a href=""><i class="fa fa-bars"></i><span>地区管理</span></a>
			<ul class="children">
				<li><a href='${pageContext.request.contextPath}/manage/area/list'>地区列表</a></li>
			</ul>
		</li>
		
		<li class="<c:if test="${param.m==7||m==7}">active </c:if> parent" >
			<a href=""><i class="fa fa-bars"></i><span>会员管理</span></a>
			<ul class="children">
				<li><a href='${pageContext.request.contextPath}/manage/member/list'>会员列表</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/memberRank/list'>会员等级</a></li>
			</ul>
		</li>
		
		<%-- <li class="<c:if test="${param.m==8||m==8}">active</c:if> parent" >
			<a href=""><i class="fa fa-bars"></i><span>内容管理</span></a>
			<ul class="children">
				<li><a href='${pageContext.request.contextPath}/manage/navigation/list'>导航管理</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/ad/list'>广告管理</a></li>
			</ul>
		</li> --%>
		<li class="<c:if test="${param.m==9||m==9}">active</c:if> parent" >
			<a href=""><i class="fa fa-bars"></i><span>优惠券管理</span></a>
			<ul class="children">
				<li><a href='${pageContext.request.contextPath}/manage/coupon/list'>优惠券管理</a></li>
			</ul>
		</li>
		<li class="<c:if test="${param.m==10||m==10}">active</c:if> parent" >
			<a href=""><i class="fa fa-bars"></i><span>车队/技师管理</span></a>
			<ul class="children">
				<li><a href='${pageContext.request.contextPath}/manage/carTeam/list'>车队管理</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/technician/list'>技师帐号管理</a></li>
			</ul>
		</li>
		<li class="<c:if test="${param.m==11||m==11}">active</c:if> parent" >
			<a href=""><i class="fa fa-bars"></i><span>新闻管理</span></a>
			<ul class="children">
				<li><a href='${pageContext.request.contextPath}/manage/news/list'>新闻管理</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/news/add'>新增新闻</a></li>


			<%-- 	<li><a href='${pageContext.request.contextPath}/user/order/list?memberId=92dea7a9-5ec7-4044-a73a-ba99b91a2af2'>订单</a></li> --%>


				<%-- <li><a href='${pageContext.request.contextPath}/web/order/list?memberId=92dea7a9-5ec7-4044-a73a-ba99b91a2af2'>订单</a></li> --%>

			</ul>
		</li>
		<li class="<c:if test="${param.m==13||m==13}">active</c:if> parent" >
			<a href=""><i class="fa fa-bars"></i><span>广告管理</span></a>
			<ul class="children">
				<li><a href='${pageContext.request.contextPath}/manage/advert/list'>广告管理</a></li>
				<li><a href='${pageContext.request.contextPath}/manage/advert/add'>添加广告</a></li>
			</ul>
		</li>
		<li class="<c:if test="${param.m==12||m==12}">active</c:if> parent" >
			<a href=""><i class="fa fa-bars"></i><span>系统设置</span></a>
			<ul class="children">
				<li><a name="memberId" href='${pageContext.request.contextPath}/manage/system/view'>系统参数</a></li>
				<li><a name="memberId" href='${pageContext.request.contextPath}/manage/server/list'>关于我们</a></li>
			</ul>
		</li>
		<li class="<c:if test="${param.m==14||m==14}">active</c:if> parent" >
			<a href=""><i class="fa fa-bars"></i><span>用户反馈</span></a>
			<ul class="children">
				<li><a name="memberId" href='${pageContext.request.contextPath}/manage/feedback/list'>用户反馈</a></li>
			</ul>
		</li>
	</ul>
</div>
<script type="text/javascript">
	
</script>
<!-- leftpanel -->