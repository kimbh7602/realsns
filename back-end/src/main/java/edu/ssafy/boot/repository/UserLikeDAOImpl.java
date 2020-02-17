package edu.ssafy.boot.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.ssafy.boot.dto.ContentVo;
import edu.ssafy.boot.dto.ImageVo;
import edu.ssafy.boot.dto.UserLikeVo;
import edu.ssafy.boot.dto.UserVo;

@Repository("UserLikeDAOImpl")
public class UserLikeDAOImpl implements IUserLikeDAO {

    @Autowired
    SqlSession session;

    @Override
    public boolean userLike(UserLikeVo userLike) {
        int insert = session.insert("ssafy.userLike.insert", userLike);
        if (insert > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean userDislike(UserLikeVo userLike) {
        int delete = session.delete("ssafy.userLike.delete", userLike);
        if (delete > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ContentVo> userLikeList(String user_id) {
        List<ContentVo> contentList = session.selectList("ssafy.userLike.userLikeList", user_id);
        for (ContentVo contentVo : contentList) {
            List<ImageVo> imageList = session.selectList("ssafy.image.imageList", contentVo.getContent_id());
            contentVo.setImageList(imageList);
            UserVo user = session.selectOne("ssafy.user.info", contentVo.getUser_id());
			if(user.getProfile_url() != null && user.getProfile_filter() != null){
				contentVo.setProfile_url(user.getProfile_url());
				contentVo.setProfile_filter(user.getProfile_filter());
			}
        }
        return contentList;
    }
}