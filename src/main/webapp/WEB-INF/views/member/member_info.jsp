<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>

   table {
   border-collapse: collapse;
   border-spacing: 0;
   border : 1px solid lightgray;
   width:70%;
   margin: 2% auto;
   }
   
   th, td{
   text-align: center;
   padding: 8px;
   }
   
   tr:nth-child(2n+1){
   background-color: #82ccdd;
   }
   td a{color: white}
</style>
</head>
<body>
	<jsp:include page="../board/header.jsp" />
	<c:set var="m" value="${memberinfo}"/> <!-- Member_infoAction에서 setAttribute로 지정한 속성값과 동일하게 value를 쓴다 -->
	<table>
		<tr>
			<td>아이디</td>
			<td>${m.id}</td>
		</tr>
		<tr>
			<td>비밀번호</td>
			<td>${m.password}</td>
		</tr>
		<tr>
			<td>이름</td>
			<td>${m.name}</td>
		</tr>
		<tr>
			<td>나이</td>
			<td>${m.age}</td>
		</tr>
		<tr>
			<td>성별</td>
			<td>${m.gender}</td>
		</tr>
		<tr>
			<td>이메일 주소</td>
			<td>${m.email}</td>
		</tr>
		<tr>
			<td colspan=2>
			<a href="member_list.net">리스트로 돌아가기</a>
			</td>
		</tr>
	</table>
</body>
</html>