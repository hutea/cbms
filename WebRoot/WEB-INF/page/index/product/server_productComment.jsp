<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/page/common/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<c:forEach var="value" items="${pageView.records }">
	<div class="comment11">
		<table class="comment1_con">
			<tbody>
				<tr>
					<td>
						<div>
							<p>
								评价：<span>${value.pcontent }</span>
							</p>
							<c:if test="${!empty value.customComment }">
								<p>
									回复：<span>${value.customComment.pcontent }</span>
								</p>	
							</c:if>
							<b><fmt:formatDate value="${value.createDate }" pattern="yyyy-MM-dd"/></b>
						</div>
					</td>
					<td class="fr">
						<div>
							会员：<span>${value.member.memberNamePhone }</span>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</c:forEach>
<div style="position:relative;height: 58px;">
	<jsp:include page="comment_fenye.jsp" />
</div>
