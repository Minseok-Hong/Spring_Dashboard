<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원가입</title>
</head>
<body>
    <center>
        <h1>회원가입 페이지</h1>
 
        <form action="join.do">
            <table>
                <tr>
                    <td>아이디</td>
                    <td><input type="text" name="userid" ></td>
                </tr>
                <tr>
                    <td>이메일</td>
                    <td><input type="text" name="email" ></td>
                </tr>
                <tr>
                    <td>비밀번호</td>
                    <td><input type="password" name="pwd"></td>
                </tr>
                <tr>
                    <td>비밀번호 확인</td>
                    <td><input type="password" name="pwd_CHECK" ></td>
                </tr>
                <tr>
                	<td>권한 (0,1,2)</td>
                    <td> <select name='authority'>
    				<option value='' selected>-- 선택 --</option>
    				<option value='0'>0</option>
    				<option value='1'>1</option>
    				<option value='2'>2</option>
				</select></td>
                <tr>
                    <td><input type="hidden" name="prikey" ></td>
                </tr>
				</tr>
                <tr>
                    <td colspan="2" align="center">
                    <input type="submit" value="가입하기"></td>
                </tr>
            </table>
 		${error}
        </form>
    </center>
    
</body>
</html>
