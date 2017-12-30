package service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import member.dto.MemberDTO;
import member.dao.MemberDAO;
/*
 * Controller에서 사용하는 메서드가 구성하는 클래스 입니다.(Service) 
 */
@Service
public class MemberService {
	
	@Autowired
	private MemberDAO memberDao;

    public static boolean isEmail(String email) {
    	//이메일 형식을 체크해주는 정규식입니다.
        if (email==null) return false;
        boolean b = Pattern.matches(
            "[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", 
            email.trim()); //공백매치
        return b;
    }
    
    public List<HashMap<String, Object>> selectAll() {
    	//DB에 존재하는 모든 내용을 가져오는 메써드입니다.
        return memberDao.selectAll();
    }
 
    public int joinMember(HashMap<String, Object> params) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException{
    		
    	if(memberDao.selectOne((String)params.get("userid")) != null){
    		return 4;
    		
    	}
    	else if(!(isEmail((String)params.get("email")))){
    		return 2;
    	}
 
    	else if(params.get("pwd").equals(params.get("pwd_CHECK")))
        {
        	if(((String) params.get("pwd")).length() < 4 ){
        		return 3;
        	}
        	
        	SecureRandom random = new SecureRandom();
        	KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        	generator.initialize(2048, random);
        	KeyPair pair = generator.generateKeyPair();
        	Key pubKey = pair.getPublic();
        	Key privKey = pair.getPrivate();
        	
        	Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPpadding");
        	cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        	
        	byte[] arrCipherData = cipher.doFinal(Base64.getDecoder().decode((String) params.get("pwd")));
        	String strCipher = new String(arrCipherData);
        	
        	System.out.println("strCipher : " + strCipher);
        	
        	cipher.init( Cipher.DECRYPT_MODE, privKey);
        	byte[] arrData = cipher.doFinal(arrCipherData);
        	String strResult = new String(arrData);
       
        	System.out.println("strResult : " + strResult);

        	String encodedKey = Base64.getEncoder().encodeToString(privKey.getEncoded());
        	System.out.println("encodedKey : " + encodedKey);
        	
        	params.put("prikey",encodedKey);
        	
        	System.out.println("params : " + params);
        	
        	params.put("pwd",Base64.getEncoder().encodeToString(arrCipherData));

        	//////////////////////////////////////////////////
        	/*
        	byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        	
        	KeyFactory kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
        	PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
        	
        	cipher.init( Cipher.DECRYPT_MODE, privateKey);
        	
        	
        	byte[] test = cipher.doFinal(arrCipherData);
        	String strtest = new String(test);
        	
        	System.out.println("strtest : " + strtest);
        	*/
        	


            memberDao.insertMember(params);

            return 1;
        }
        else{
        	return 0;
        }
		
    }
    
    
    public int login(String id, String pwd) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException{

        HashMap<String, Object> result = memberDao.selectOne(id);
	    
        String encodedKey = (String) result.get("prikey");
        byte[] arrCipherData = (Base64.getDecoder().decode((String) result.get("PWD")));
        
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPpadding");
        
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
    	
    	KeyFactory kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
    	PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
    	
    	cipher.init( Cipher.DECRYPT_MODE, privateKey);
    	
    	byte[] test = cipher.doFinal(arrCipherData);
    	String oPwd = Base64.getEncoder().encodeToString(test);
    	
    	System.out.println("Login pwd : " + pwd);
    	
        if(result == null){
        	return 0;
        }
        	
            
        else{

        	//String oPwd = ((String) result.get("PWD"));
        	
        	int oAuthority = (Integer) result.get("AUTHORITY");

            if(oPwd==null){
            	System.out.println("회원가입이 되어있지 않습니다.");
                return 2;//회원가입이 되어있지 않다.
            }
            else{
                if(oPwd.equals(pwd)){
                	if(oAuthority == 2){
                		System.out.println(" return 1  ");
                		return 1;//정상적인 로그인 with admin
                	}
                	else if(oAuthority == 0){
                		System.out.println("Authority ===== User");
                		return 4;//정상적인 로그인 with user
                	}
                	else{
                		System.out.println(" return 5  ");
                		return 5;
                	}
                }           
                else
                    return 3;//비밀번호 실패
            }
            
        }
    }
    public HashMap<String, Object> getMemberInfo(String id){
        return memberDao.selectOne(id);
    }
    
    public void memberUpdate(HashMap<String, Object> params){
 
        if(params.get("pwd").equals(params.get("pwd_CHECK"))){
            HashMap<String, Object> record = memberDao.selectOne((String)params.get(MemberDTO.Member.USERID));
            record.putAll(params); 
            memberDao.updateMember(record);
        }
    }
    
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
