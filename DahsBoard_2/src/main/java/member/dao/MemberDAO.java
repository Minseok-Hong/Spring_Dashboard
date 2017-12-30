package member.dao;

import java.util.HashMap;
import java.util.List;

public interface MemberDAO {
	
    public int insertMember(HashMap<String, Object> params);
    public int updateMember(HashMap<String, Object> params);
    public int deleteMember(String id);
    public HashMap<String, Object> selectOne(String id);
    public List<HashMap<String, Object>> selectAll();
    
}
/*
 기본적으로 DB를 접근할 떄 DB를 관리 해주는 메써드들 정의 입니다.
 */