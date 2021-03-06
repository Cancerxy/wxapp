package com.zczp.service_yycoder.impl;

import com.alibaba.fastjson.JSONObject;
import com.zczp.dao.TbAskReplyMapper;
import com.zczp.dao.TbCommentMapper;
import com.zczp.dao.TbPostMapper;
import com.zczp.entity.TbAskReply;
import com.zczp.entity.TbComment;
import com.zczp.service_yycoder.AskReplyService;
import com.zczp.vo_yycoder.CollectPostDetailVo;
import com.zczp.vo_yycoder.MyAskReplyVo;
import com.zczp.vo_yycoder.TbCommentsVo;
import com.zczp.vo_yycoder.UserAskReplyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AskReplyServiceImpl implements AskReplyService {
    @Autowired(required = false)
    private TbAskReplyMapper tbAskReplyMapper;

    @Autowired(required = false)
    private TbCommentMapper tbCommentMapper;

    @Autowired(required = false)
    private TbPostMapper tbPostMapper;

    @Autowired(required = false)
    private CommonServiceImpl commonService;

    //collectPostDeatailVo只是比postDeatilVo类多了个state
    CollectPostDetailVo collectPostDetailVo;

    //评论表，每个评论表含提问和回复
    List<TbCommentsVo> CommentsVoList = null;
    List<TbCommentsVo> tbCommentsVoList = null;

    //评论表对象
    TbCommentsVo tbCommentsVo = new TbCommentsVo();

    //定义一个静态公共变量用来记录回复的数量
    static int replyCount = 0;
    Map<String, Object> map = new HashMap<String, Object>();

    /**
     * @param openId 用户的openId
     * @return
     */
    @Override
    public List<MyAskReplyVo> getMyAskReplyList(String openId) {
        commonService.transCountToDB();
        TbComment tbComment = new TbComment();
        //含评论表和post表
        List<MyAskReplyVo> myAskReplyVoList = new ArrayList<MyAskReplyVo>();
        //根据open_id去TbAskReply找到post的先后顺序，返回TbAskReply
        List<TbAskReply> askReplyList = tbAskReplyMapper.getAskReplyByOpenId(openId);
        tbComment.setFromId(openId);
        //遍历循环askReplyList，拿到对应的postId(默认是按评论更新的时间先后)
        for (TbAskReply tbAskReply : askReplyList) {
            tbCommentsVoList = new ArrayList<TbCommentsVo>();
            tbComment.setPostId(tbAskReply.getPostId());
            //通过postId和openId，查询post下对应的所有提问(reply=null,提问时间排序)
            CommentsVoList = tbCommentMapper.getCommentByPostId(tbComment.getPostId());
            int index = 0;
            while (CommentsVoList.size() > index) {
                tbComment.setReplyId(CommentsVoList.get(index).getCommentId());
                tbComment.setToId(CommentsVoList.get(index).getFromId());
                //查询
                tbCommentsVo.setCommentList(tbCommentMapper.selectCommentList(tbComment));

                CommentsVoList.get(index).setCommentList(tbCommentsVo.getCommentList());
                CommentsVoList.get(index).setReplyState(tbCommentsVo.getCommentList().size()>1?1:0);
                if (CommentsVoList.get(index).getCommentList().size() > 0
                        | CommentsVoList.get(index).getFromId().equals(openId)) {
                    tbCommentsVoList.add(CommentsVoList.get(index));
                }
                index++;
            }
            collectPostDetailVo = tbPostMapper.getPostDetailById(tbAskReply.getPostId());

            MyAskReplyVo myAskReplyVo = new MyAskReplyVo();
            myAskReplyVo.setPostDetailList(collectPostDetailVo);
            myAskReplyVo.setCommentList(tbCommentsVoList);
            myAskReplyVoList.add(myAskReplyVo);
        }
        return myAskReplyVoList;

    }

    @Override
    public List<MyAskReplyVo> getMyReplyMsgList(String openId) {
        commonService.transCountToDB();
        replyCount = 0;
        TbComment tbComment = new TbComment();
        List<MyAskReplyVo> myAskReplyVoList = new ArrayList<MyAskReplyVo>();
        //去问答表找openId 和postId
        List<TbAskReply> askReplyList = tbAskReplyMapper.getAskReplyByOpenId(openId);
        tbComment.setFromId(openId);
        for (TbAskReply tbAskReply : askReplyList) {
            tbCommentsVoList = new ArrayList<TbCommentsVo>();

            tbComment.setPostId(tbAskReply.getPostId());
            CommentsVoList = tbCommentMapper.selectTbCommentList(tbComment);
            int index = 0;
            while (CommentsVoList.size() > index) {
                tbComment.setReplyId(CommentsVoList.get(index).getCommentId());
                tbComment.setToId(openId);
                tbCommentsVo.setCommentList(tbCommentMapper.selectCommentList(tbComment));
                CommentsVoList.get(index).setCommentList(tbCommentsVo.getCommentList());
                CommentsVoList.get(index).setReplyState(tbCommentsVo.getCommentList().size()>1?1:0);
                if (CommentsVoList.get(index).getCommentList().size() > 0) {
                    replyCount += CommentsVoList.get(index).getCommentList().size();
                    tbCommentsVoList.add(CommentsVoList.get(index));
                }

                index++;

            }
            collectPostDetailVo = tbPostMapper.getPostDetailById(tbAskReply.getPostId());
            MyAskReplyVo myAskReplyVo = new MyAskReplyVo();
            myAskReplyVo.setPostDetailList(collectPostDetailVo);
            myAskReplyVo.setCommentList(tbCommentsVoList);
            if (myAskReplyVo.getCommentList() != null & myAskReplyVo.getCommentList().size() != 0) {
                myAskReplyVoList.add(myAskReplyVo);
            }
        }
        //QuickSort.quickSort(myAskReplyVoList.toArray(new MyAskReplyVo[myAskReplyVoList.size()]),0,myAskReplyVoList.size()-1);
        return myAskReplyVoList;
    }

    @Override
    public Integer getMyReplyCount(String openId) {
        getMyReplyMsgList(openId);

        return replyCount;
    }

    //删除评论
    @Override
    public int deleteTbCommentBycommentId(Integer commentId) {
        return tbCommentMapper.deleteByPrimaryKey(commentId);
    }

    @Override
    @Transactional
    public int deleteTbComment(String openId, Integer postId) {
        List<Integer> list = tbCommentMapper.getAllCommentId(openId, postId);
        for (Integer index : list) {
            tbCommentMapper.deleteTbCommentById(index);
        }
        tbAskReplyMapper.deleteById(openId, postId);
        return 1;
    }

    @Override
    public List<UserAskReplyVo> getUserAskReplyList(String openId) {
        TbComment tbComment = new TbComment();
        //含评论表和post表
        List<UserAskReplyVo> userAskReplyVoList = new ArrayList<UserAskReplyVo>();
        //根据open_id去TbAskReply找到post的先后顺序，返回TbAskReply
        List<TbAskReply> askReplyList = tbAskReplyMapper.getAskReplyByOpenId(openId);
        tbComment.setFromId(openId);
        for (TbAskReply tbAskReply : askReplyList) {
            tbComment.setPostId(tbAskReply.getPostId());
            List<TbComment> userCommentList = tbCommentMapper.getUserCommentList(tbComment);
            collectPostDetailVo = tbPostMapper.getPostDetailById(tbAskReply.getPostId());
            String collectPostDetail = JSONObject.toJSONString(collectPostDetailVo);
            int index = 0;
            while (userCommentList.size() > index) {
                UserAskReplyVo userAskReplyVo = JSONObject.parseObject(collectPostDetail, UserAskReplyVo.class);
                userAskReplyVo.setCommentId(userCommentList.get(index).getCommentId());
                userAskReplyVo.setContent(userCommentList.get(index).getContent());
                if (userCommentList.get(index).getReplyId() != null) {
                    userAskReplyVo.setIsReply(1);
                } else {
                    userAskReplyVo.setIsReply(0);
                }
                userAskReplyVoList.add(userAskReplyVo);
                index++;
            }
        }
        return userAskReplyVoList;
    }
}


