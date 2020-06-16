<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원관리 시스템 회원수정 페이지</title>
<link href="resources/css/join.css" rel="stylesheet" type="text/css">
<script src="resources/js/jquery-3.5.0.js"></script>
</head>
<body>
	<jsp:include page="../board/header.jsp" />
	<c:set var="m" value="${memberinfo }" />
	<form name="joinform" action="updateProcess.net" method="post">
		<h1>회원 수정 페이지</h1>
		<hr>
		<b>아이디</b> <input type="text" name="id" 
			value="${m.id}" readonly> <!-- id는 수정하면 안되니까 readonly 사용 -->
		<b>비밀번호</b> <input
			type="password" name="pass" maxlength=10
			value="${m.password }" readonly> 
		<b>이름</b> <input
			type="text" name="name" value="${m.name }" placeholder="Enter name" required> 
		<b>나이</b> <input type="text" name="age"
			value="${m.age }" placeholder="Enter age" maxlength=2 required>
		<b>성별</b>
		<div>
			<input type="radio" name="gender" value="남" checked><span>남자</span>
			<input type="radio" name="gender" value="여"><span>여자</span>
		</div>
		<b>이메일 주소</b> <input type="text" name="email" value="${m.email }" placeholder="Enter email" required> 
		<span id="email_message"></span>
		<div class="clearfix">
			<button type="submit" class="submitbtn">수정</button>
			<button type="reset" class="cancelbtn">취소</button>
		</div>
	</form>
	<script>
	//성별체크해주는 부분
	$("input[value='${m.gender}']").prop('checked',true);
	
	//취소버튼 => 이전 페이지로 돌아가기
	$(".cancelbtn").click(function(){
		history.back();
	});
	
	//처음 화면 로드시 보여줄 이메일은 이미 체크 완료된 것이므로 기본 checkmail=true입니다.
	var checkmail = true;
	$('input:eq(6)').on('keyup', function(){
        $("#email_message").empty();
        //[A-Za-z0-9_]와 동일한 것이 \w
        //+는 1회 이상 반복을 의미합니다. {1,}와 동일합니다.
        //\w+는 [A-Za-z0-9_]를 1개이상 사용하라는 의미
        var pattern = /^\w+@\w+[.]\w{3}$/;
        var email = $("input:eq(6)").val();
        if(!pattern.test(email)){
           $("#email_message").css('color', 'red').html("이메일형식이 맞지 않습니다.");
           checkemail = false;   
        } else {
           $("#email_message").css('color', 'green').html("이메일형식에 맞습니다.");
           checkemail = true;   
        }
     });   //email keyup 이벤트 처리 끝 
     
	$('form').submit(function(){
		if(!$.isNumeric($("input[name='age']").val())) {
			alert("나이를 숫자로 입력하세요");
			$("input:eq(3)").focus();
			return false;
		}
		
		if(!checkemail){
			alert("email 형식을 확인하세요");
			$("input:eq(6)").focus();
			return false;
		}
		
	});
	</script>
</body>
</html>