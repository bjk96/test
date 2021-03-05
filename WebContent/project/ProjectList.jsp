<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>      
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>프로젝트 목록</title>
</head>
<body>
<jsp:include page="/Header.jsp"/>
<h1>프로젝트 목록</h1>
<p><a href='add.do'>신규 프로젝트</a></p>
<table border="1">
<c:if test="${listModel.totalPageCount > 0}">
	<tr>
		<td colspan="6">
			${listModel.startRow}-${listModel.endRow}
			[${listModel.requestPage}/${listModel.totalPageCount}]
		</td>
	</tr>
</c:if>
	<tr>
		<th style="text-align:center;">번호</th>
		<th style="text-align:center;">제목</th>
		<th style="text-align:center;">시작일</th>
		<th style="text-align:center;">종료일</th>
		<th style="text-align:center;">상태</th>
		<th style="text-align:center;"></th>
	</tr>
	<c:choose>
	<c:when test="${listModel.hasProject == false}">
		<tr>
			<td colspan="6">
				프로젝트가 없습니다.
			</td>
		</tr>
	</c:when>
	<c:otherwise>
		<c:forEach var="project" items="${listModel.projectList}">
			<tr>
				<td>${project.no}</td>
				<td><a href='update.do?no=${project.no}'>${project.title}</a></td>
				<td>${project.startDate}</td>
				<td>${project.endDate}</td>
				<td>${project.state}</td>
				<td><a href='delete.do?no=${project.no}'>[삭제]</a><br></td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="6">
				<c:if test="${beginPage > pagePerBoard}">
					<a href="<c:url value="/project/list.do?p=${beginPage-1}"/>">이전</a>
				</c:if>
				<c:forEach var="pno" begin="${beginPage}" end="${endPage}">
					<a href="<c:url value="/project/list.do?p=${pno}" />">[${pno}]</a>
				</c:forEach>
				<c:if test="${endPage < listModel.totalPageCount}">
					<a href="<c:url value="/project/list.do?p=${endPage + 1}"/>">다음</a>
				</c:if>
			</td>
		</tr>
	</c:otherwise>
	</c:choose>
</table>
<jsp:include page="/Tail.jsp"/>
</body>
</html>