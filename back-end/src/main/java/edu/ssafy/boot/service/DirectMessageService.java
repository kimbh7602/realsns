package edu.ssafy.boot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.ssafy.boot.dto.DirectMessageVo;
import edu.ssafy.boot.dto.UserDmVo;
import edu.ssafy.boot.repository.IDirectMessageDAO;

@Service("DirectMessageService")
public class DirectMessageService implements IDirectMessageService {

    @Autowired
    @Qualifier("DirectMessageDAOImpl")
    IDirectMessageDAO dao;

    @Override
    public boolean insertDirectMessage(DirectMessageVo directMessage) {
        return dao.insertDirectMessage(directMessage);
    }

    @Override
    public boolean deleteDirectMessage(DirectMessageVo directMessage) {
        return dao.deleteDirectMessage(directMessage);
    }

    @Override
    public List<DirectMessageVo> directMessageList(UserDmVo userDm) {
        return dao.directMessageList(userDm);
    }

    @Override
    public boolean directMessageReadCheck(UserDmVo userDm) {
        return dao.directMessageReadCheck(userDm);
    }

    @Override
    public int directMessageUnReadCnt(UserDmVo userDm, String user_id) {
        return dao.directMessageUnReadCnt(userDm, user_id);
    }

    @Override
    public void deleteOldMessage() {
        dao.deleteOldMessage();
    }
}