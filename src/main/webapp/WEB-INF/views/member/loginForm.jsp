<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>회원관리 시스템 로그인 페이지</title>
<%--context 경로(프로젝트이름) 기분으로 위치를 잡는다 --%>
<link href="resources/css/login.css" type="text/css" rel="stylesheet">
<script src="resources/js/jquery-3.5.0.js"></script>
<script>
$(function(){
  	$(".join").click(function(){
  		location.href="join.net";
  	});
})
</script>
</head>
<body>

	<form name="loginform" action="loginProcess.net" method="post">
		<h1>로그인</h1>
		<hr>
		<b>아이디</b> <input type="text" id="id" name="id" placeholder="Enter id"
			required
		<c:if test="${!empty saveid }">value="${saveid }"</c:if> >
		 <b>Password</b> <input type="password" name="password"
			placeholder="Enter password" required> <label> <input
			type="checkbox" name="remember" style="margin-bottom: 15px"
			<c:if test="${!empty saveid }">
         checked
         </c:if>>
			Remember me
		</label>
		<div class="clearfix">
			<button type="submit" class="submitbtn">로그인</button>
			<button type="button" class="join">회원가입</button>
		</div>
	</form>

</html>