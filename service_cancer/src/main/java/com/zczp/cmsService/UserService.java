package com.zczp.cmsService;

import com.zczp.util.PageQueryUtil;
import com.zczp.util.PageResult;
import com.zczp.vo_yycoder.CollectPostDetailVo;
import com.zczp.vo_yycoder.PostDetailVo;
import com.zczp.vo_yycoder.UserDetailVo;

import java.util.List;

public interface UserService {

    //根据登录传来的id获取用户的信息
    UserDetailVo getUserByOpenId(String openId);

    //用户修改个人信息
    int updateUserIfoById(UserDetailVo userDetailVo);

    //用户查看个人收藏
    List<CollectPostDetailVo> getUserCollection(String openId);

    //删除个人收藏
    int deleteCollection(String openId, Integer postId);

    //查看个人发布信息
    List<PostDetailVo> getUserIssue(String openId);

    //删除个人发布信息
    int deleteUserIssue(String openId, Integer postId);

    //获取所有用户的信息
    PageResult getAllUser(PageQueryUtil pageUtil);

    //根据昵称查找用户
    UserDetailVo searchUserByName(String userName);

    //销户操作（删除用户信息）
    int deleteUserById(String openId);
}
