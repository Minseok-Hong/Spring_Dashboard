<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>D A S H B O A R D</title>
</head>
<body>
    <center>
        <h1>D A S H B O A R D(ADMIN)</h1>
 
        <form action="dashBoardForm.do">
           		<tr>
                    <td colspan="2" align="char">
                        <input type="button" value="로그아웃" onclick = "location.href = 'logout.do'"> 
                        <input type="button" value="회원정보수정" onclick = "location.href = 'memberUpdateForm.do'">
                        <input type="button" value="회원가입" onclick = "location.href = 'joinForm.do'">
                    </td>
                </tr>
 
        </form>
    </center>
</body>
</html>
