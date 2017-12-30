<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String ctxPath = (String) request.getContextPath();
	System.out.println("ctxPath :" + ctxPath);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript" src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
<!-- 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jsbn.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/rsa.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/prng4.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/rng.js"></script>

<script type="text/javascript" src="/resources/js/jsbn.js"></script>
<script type="text/javascript" src="/resources/js/rsa.js"></script>
<script type="text/javascript" src="/resources/js/prng4.js"></script>
<script type="text/javascript" src="/resources/js/rng.js"></script>
-->

<script type="text/javascript" src="<%=ctxPath %>/resources/js/jsbn.js"></script>
<script type="text/javascript" src="<%=ctxPath %>/resources/js/rsa.js"></script>
<script type="text/javascript" src="<%=ctxPath %>/resources/js/prng4.js"></script>
<script type="text/javascript" src="<%=ctxPath %>/resources/js/rng.js"></script>

<script type="text/javascript">    

    function login(){   	
      
        var id = $('#id');
        var pw = $("#pwd");
        
        if(id.val() == ""){
            alert("아이디를 입력 해주세요.");
            id.focus();
            return false;
        }
        
        if(pw.val() == ""){
            alert("비밀번호를 입력 해주세요.");
            pw.focus();
            return false;
        }
        // rsa 암호화

        var rsa = new RSAKey();
        rsa.setPublic($('#RSAModulus').val(),$('#RSAExponent').val());
        
        $("#USER_ID").val(rsa.encrypt(id.val()));
        $("#USER_PW").val(rsa.encrypt(pw.val()));
        
        //$("#id").val(rsa.encrypt(id.val()));
        //$("#pwd").val(rsa.encrypt(pw.val()));
        
        //id.val("");
        //pw.val("");

        //$("#frm").submit();
        return true;

    }
</script>

<title>login</title>
</head>
<body>
    <center>
        <form name = "frm" id="frm" action = "login.do" method="post" onsubmit = "return login()"> <!--  -->
        <input type="hidden" id="RSAModulus" value="${RSAModulus}" /><!-- 서버에서 전달한값을 셋팅한다. -->
		<input type="hidden" id="RSAExponent" value="${RSAExponent}" /><!-- 서버에서 전달한값을 셋팅한다. -->
        <input type="text" id="RSAModulus" value="${RSAModulus}" /><!-- 서버에서 전달한값을 셋팅한다. -->
		<input type="text" id="RSAExponent" value="${RSAExponent}" /><!-- 서버에서 전달한값을 셋팅한다. -->
		  <input type="text" id="id" name="id"/>
		     <input type="password" id="pwd" name="pwd"/>
		      <input type="hidden" id="USER_ID" name="USER_ID">
		      <input type="hidden" id="USER_PW" name="USER_PW">
		      <input type="submit" value="로그인(기존)"/>
		      <input type="button" value="로그인(대안2)" onclick ="login()"/>
                        <button type="button" onclick ="login()">로그인(대안3)</button>
		   <!-- 
		      
            <table>
                <tr>
                    <td width="150px" align="center">아이디 :</td>
                    <td width="150px" align="center">
                      
                    </td>
                </tr>
                <tr>
                    <td width="150px" align="center">비밀번호 :</td>
                    <td width="150px" align="center">
                     
                    </td>
                </tr>
                <tr>
                	<td width="150px" align="center">
                		<input type="hidden" id="USER_ID" name="USER_ID">
                	</td>
                	<td width="150px" align="center">
			       		<input type="hidden" id="USER_PW" name="USER_PW">
			       	</td>
                </tr>
                <tr>
                	<td align="center"></td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                     	<button onclick="login()">로그인test</button>
                       
                        <input type="button" value="로그인(대안2)" onclick ="login()"/>
                        <button type="button" onclick ="login()">로그인(대안3)</button>
                        
                    </td>
                </tr>
            </table>
             -->
            ${message}
        </form>
    </center>
    <button onclick ="login()">로그인(대안1)</button>
    
</body>
</html>
