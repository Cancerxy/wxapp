package com.zczp.service_yycoder.impl;

import com.zczp.dao.*;
import com.zczp.service_yycoder.UserService;
import com.zczp.util.MathUtils;
import com.zczp.util.RedisKeyUtil;
import com.zczp.util.RedisUtil;
import com.zczp.vo_yycoder.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private TbUserMapper tbUserMapper;

    @Autowired(required = false)
    private TbCollectMapper tbCollectMapper;

    @Autowired(required = false)
    private TbPostMapper tbPostMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;

    @Autowired
    RedisUtil redisUtil;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDetailVo getUserByOpenId(String openId) {
        UserDetailVo userDetailVo = tbUserMapper.getUserByOpenId(openId);
        if (userDetailVo != null)
            return userDetailVo;
        return null;
    }

    @Override
    @Transactional
    public int addRobotUserIfo(UserDetailVo userDetailVo) {
        String s = MathUtils.makeUpNewData(Thread.currentThread().hashCode()+"", 3)+   MathUtils.randomDigitNumber(7);
        userDetailVo.setOpenId(s);
        userDetailVo.setUserGender("男");
        userDetailVo.setState(1);
        return tbUserMapper.addRobotUserIfo(userDetailVo);
    }

    @Override
    public int updateUserIfoById(UserDetailVo userDetailVo) {
        return tbUserMapper.updateUserInfoById(userDetailVo);
    }

//    @Override
//    public List<CollectPostDetailVo> getUserCollection(String openId) {
//        Set<String> keys = redisUtil.keys("collect:" + openId + ":*");
//        List<CollectPostDetailVo> collectPostDetailVoList= new ArrayList<>();
//        if(keys == null || keys.size()==0){
//            collectPostDetailVoList= tbCollectMapper.getPostDetailById(openId);
//            Integer index = 0;
//            String s="collect:" + openId + ":";
//            for(CollectPostDetailVo c:collectPostDetailVoList){
//                String key=s+c.getPostId();
//                redisCacheTemplate.opsForValue().set(key, (Serializable) c);
//            }
//            log.info("从数据库中查找。。。");
//        }else{
//            for(String key:keys){
//                collectPostDetailVoList.add((CollectPostDetailVo) redisCacheTemplate.opsForValue().get(key));
//            }
//            log.info("从缓存中查找。。。");
//        }
//        return collectPostDetailVoList;
//
//    }

    @Override
    public List<CollectPostDetailVo> getUserCollection(String openId) {
        List<CollectPostDetailVo> collectPostDetailVoList= tbCollectMapper.getPostDetailById(openId);
        List<CollectPostDetailVo> list = new ArrayList<>();
        for(CollectPostDetailVo collectPostDetailVo:collectPostDetailVoList){
            String key = RedisKeyUtil.getKey(openId,collectPostDetailVo.getPostId());
            if(key==null||key.equals("")){
                break;
            }else{
                collectPostDetailVo.setState(StringToInteger(key));
                if (collectPostDetailVo.getState()!=0){
                    list.add(collectPostDetailVo);
                }
            }

        }

        return list;

    }
    private Integer StringToInteger(String key){
        try {
            return Integer.parseInt(redisUtil.hget(RedisKeyUtil.MAP_KEY_COLLECT,key));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    @Override
    @Transactional
    public int deleteCollection(String openId, Integer postId) {
        String key = RedisKeyUtil.getKey(openId,postId);
        if(key!=null)
            redisUtil.hset(RedisKeyUtil.MAP_KEY_COLLECT,key,"0");
        else
            tbCollectMapper.deleteCollectionById(openId, postId);

        return 1;
    }

    @Override
    @Transactional
    public List<PostDetailVo> getUserIssue(String openId) {
        List<PostDetailVo> postDetailVoList = tbPostMapper.getPostIssueByOpenId(openId);
        return postDetailVoList;
    }

    @Override
    @Transactional
    public int deleteUserIssue(String openId, Integer postId) {
        return tbPostMapper.deleteUserIssueById(openId, postId);
    }

    @Override
    public List<UserDetailVo> getAllUser(Integer state) {
        return tbUserMapper.getAllUser(state);
    }

    @Override
    public List<UserDetailVo> searchUserByName(String userName) {
        return tbUserMapper.seachUserByName(userName);
    }

    @Override
    public int deleteUserById(String openId) {
        return tbUserMapper.deleteByPrimaryKey(openId);
    }
}
