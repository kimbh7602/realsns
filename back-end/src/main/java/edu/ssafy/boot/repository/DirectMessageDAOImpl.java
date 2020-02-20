package edu.ssafy.boot.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.ssafy.boot.dto.DirectMessageVo;
import edu.ssafy.boot.dto.UserDmVo;

@Repository("DirectMessageDAOImpl")
public class DirectMessageDAOImpl implements IDirectMessageDAO {

    @Autowired
    SqlSession session;

    @Override
    public boolean insertDirectMessage(DirectMessageVo directMessage) {
        int insert = session.insert("ssafy.directMessage.insert", directMessage);
        if (insert > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean deleteDirectMessage(DirectMessageVo directMessage) {
        int delete = session.delete("ssafy.directMessage.delete", directMessage);
        if (delete > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<DirectMessageVo> directMessageList(UserDmVo userDm) {
        return session.selectList("ssafy.directMessage.directMessageList", userDm);
    }

    @Override
    public boolean directMessageReadCheck(UserDmVo userDm) {
        int update = session.update("ssafy.directMessage.update", userDm);
        if (update > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int directMessageUnReadCnt(UserDmVo userDm, String user_id) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("dm_id", userDm.getDm_id());
    	map.put("user_id", user_id);
        return session.selectOne("ssafy.directMessage.unReadCnt", map);
    }

    @Override
    public void deleteOldMessage() {
        session.delete("ssafy.directMessage.deleteOldMessage");
    }

	@Override
	public int allUnReadCnt(String user_id) {
		int cnt = session.selectOne("ssafy.directMessage.allUnReadCnt", user_id);
		return cnt;
	}
}