package web.app.com;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import service.MemberService;

@Controller
public class HomeController {
	
	private static String _RSA_WEB_KEY_ = "_RSA_WEB_KEY_";

	@Autowired(required = true)
    private MemberService service;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpServletRequest request, HttpServletResponse response, Locale locale, Model model, HttpSession session) throws NoSuchAlgorithmException, InvalidKeySpecException {

		session = request.getSession();

    	KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    	generator.initialize(2048);
    	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    	KeyPair Keypair = generator.generateKeyPair();
    	Key publicKey = Keypair.getPublic();
    	Key privateKey = Keypair.getPrivate();
    	
    	
    	session.setAttribute("_RSA_WEB_KEY_", privateKey);
    	session.setAttribute(HomeController._RSA_WEB_KEY_, privateKey);
    	
    	RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
		String publicKeyModulus = publicSpec.getModulus().toString(16);
		String publicKeyExponent = publicSpec.getPublicExponent().toString(16);

		request.setAttribute("RSAModulus", publicKeyModulus);  //로그인 폼에 Input Hidden에 값을 셋팅하기위해서
		request.setAttribute("RSAExponent", publicKeyExponent);   //로그인 폼에 Input Hidden에 값을 셋팅하기위해서
		
		System.out.println("RSAModulus :" + publicKeyModulus);
		System.out.println("RSAExponent :" + publicKeyExponent);

    	//////////////////////////////////
		return "loginForm";
	}
  
    @RequestMapping("loginForm.do")
    public String loginForm(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws NoSuchAlgorithmException, InvalidKeySpecException{
    	
    	session = request.getSession();

        /////////////////////////
    	KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    	generator.initialize(2048);
    	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    	KeyPair Keypair = generator.generateKeyPair();
    	Key publicKey = Keypair.getPublic();
    	Key privateKey = Keypair.getPrivate();
    	
    	System.out.println("First privateKey : " + privateKey);
    	
    	session.setAttribute("_RSA_WEB_KEY_", privateKey);
    	System.out.println("First privateKey : " + request.getAttribute("_RSA_WEB_KEY_"));
    	
    	RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
		String publicKeyModulus = publicSpec.getModulus().toString(16);
		String publicKeyExponent = publicSpec.getPublicExponent().toString(16);

		request.setAttribute("RSAModulus", publicKeyModulus);  //로그인 폼에 Input Hidden에 값을 셋팅하기위해서
		request.setAttribute("RSAExponent", publicKeyExponent);   //로그인 폼에 Input Hidden에 값을 셋팅하기위해서
		
		System.out.println("RSAModulus :" + publicKeyModulus);
		System.out.println("RSAExponent :" + publicKeyExponent);
		
        return "loginForm";
    }
 
    @RequestMapping("joinForm.do")
    public void joinForm(){
        
    }
    
    @RequestMapping("dashBoardForm.do")
    public String dashBoardForm(){
        return "dashBoardForm";
    }
    
    @RequestMapping("dashBoardForm_user.do")
    public String dashBoardForm_user(){
        return "dashBoardForm_user";
    }
    
    
    @RequestMapping("join.do")
    public String join(@RequestParam HashMap<String, Object> params, HttpSession session) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException
    {
        System.out.println(params);
        
        if((service.joinMember(params))==4){
        	session.setAttribute("error", "같이 아이디가 존재합니다.");
        	return "redirect:joinForm.do";
        }
        else if((service.joinMember(params))==2){
        	
        	session.setAttribute("error", "이메일 형식을 잘못 입력하셨습니다.");
        	return "redirect:joinForm.do";
        }
        else if ((service.joinMember(params))==3){
        	System.out.println("비민번호 길이 오류");
        	session.setAttribute("error", "비밀번호의 길이는 최소 4글자 입니다.");
        	return "redirect:joinForm.do";
        }
        else{
        	session.setAttribute("error", "");
        	return "redirect:loginForm.do";
        }
    }
    

    @RequestMapping("login.do")
    public ModelAndView login(HttpServletRequest request,String id, String pwd,String USER_ID,String USER_PW) throws Exception{

        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession();
        session.setAttribute("message", "");
        
        String userId = (String) request.getParameter("USER_ID");
        String userPw = (String) request.getParameter("USER_PW");
        
        String userId_1 = (String) request.getParameter("id");
        String userPw_1 = (String) request.getParameter("pwd");
        
        PrivateKey privateKey = (PrivateKey) session.getAttribute("_RSA_WEB_KEY_");
        System.out.println("Return type : " + session.getAttribute("_RSA_WEB_KEY_").getClass().getName());
        
        System.out.println("privateKey : " + privateKey);
        
        System.out.println("USER_ID.RSA : " + userId);
        System.out.println("USER_PW.RSA : " + userPw);
        
        System.out.println("id.RSA : " + userId_1);
        System.out.println("pwd.RSA : " + userPw_1);
        
        id = decryptRsa(privateKey, userId);
        pwd = decryptRsa(privateKey, userPw);

        System.out.println("USER_ID.RSA.des : " + id);
        System.out.println("USER_PW.RSA.des : " + pwd);    
        
        
    	System.out.println("service  " + service.login(id, pwd));
        if(service.login(id, pwd)==1){
            session.setAttribute("userid", id);
            //System.out.println(" redirect:dashBoardForm.do ");
            mav.setViewName("redirect:dashBoardForm.do");
        }
        else if(service.login(id, pwd)==3){
        	session.setAttribute("message", "비밀번호를 잘못 입력하셨습니다.");
        	//System.out.println("redirect:loginForm.d ");
            mav.setViewName("redirect:loginForm.do");
        	
        }
        else if(service.login(id, pwd)==4){
            session.setAttribute("userid", id);
            //System.out.println("redirect:dashBoardForm_user.d");
            mav.setViewName("redirect:dashBoardForm_user.do");
        }
        
        else{
        	session.setAttribute("message", "");
        	//System.out.println("redirect:loginForm.d");
            mav.setViewName("redirect:loginForm.do");
        }
        return mav;
    
    }    
    
    @RequestMapping("logout.do")
    public String logout(HttpSession session){
        session.removeAttribute("userid");
        session.setAttribute("message", "");
        return "redirect:loginForm.do";
        
    }
    @RequestMapping("memberUpdateForm.do")
    public String memberUpdateForm(Model model,HttpSession session){
        String userid = (String) session.getAttribute("userid");
        if(userid == null)
            return "redirect:loginForm.do";
        model.addAllAttributes(service.getMemberInfo(userid));
        return "memberUpdateForm";
    }
    @RequestMapping("memberUpdate.do")
    public String memberUpdate(@RequestParam HashMap<String, Object> params){
        service.memberUpdate(params);
        return "redirect:dashBoardForm.do";
    }
    
    public String decryptRsa(PrivateKey privateKey, String securedValue) {
   	 String decryptedValue = "";
   	 try{
   		Cipher cipher = Cipher.getInstance("RSA");
   	   /**
   		* 암호화 된 값은 byte 배열이다.
   		* 이를 문자열 폼으로 전송하기 위해 16진 문자열(hex)로 변경한다. 
   		* 서버측에서도 값을 받을 때 hex 문자열을 받아서 이를 다시 byte 배열로 바꾼 뒤에 복호화 과정을 수행한다.
   		*/
   		byte[] encryptedBytes = hexToByteArray(securedValue);
   		cipher.init(Cipher.DECRYPT_MODE, privateKey);
   		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
   		decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
   	 }catch(Exception e)
   	 {
   	 }
   		return decryptedValue;
   } 


   /** 
    * 16진 문자열을 byte 배열로 변환한다. 
    */
    public static byte[] hexToByteArray(String hex) {
    	if (hex == null || hex.length() % 2 != 0) {
    		return new byte[]{};
    	}
    
    	byte[] bytes = new byte[hex.length() / 2];
    	for (int i = 0; i < hex.length(); i += 2) {
    		byte value = (byte)Integer.parseInt(hex.substring(i, i + 2), 16);
    		bytes[(int) Math.floor(i / 2)] = value;
    	}
    	return bytes;
   }
    

	
	
}
