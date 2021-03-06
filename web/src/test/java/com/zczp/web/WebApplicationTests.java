package com.zczp.web;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zczp.dao.*;
import com.zczp.entity.TbAskReply;
import com.zczp.entity.TbComment;
import com.zczp.service_cancer.Impl.TbCompanyServiceImpl;
import com.zczp.vo_cancer.CommentsVo;
import com.zczp.vo_cancer.CompanyVo;
import com.zczp.vo_yycoder.CollectPostDetailVo;
import com.zczp.vo_yycoder.MyAskReplyVo;
import com.zczp.vo_yycoder.TbCommentsVo;
import com.zczp.vo_yycoder.UserDetailVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.PortableServer.REQUEST_PROCESSING_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebApplicationTests {



  @Test
  public void test(){
    Date date = new Date();
    SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    System.out.println(dateFormat.format(date));
    System.out.println(date);
  }
//MD5加密
  @Test
  public void getEncryptedPassword(){
    try{
      MessageDigest md5=MessageDigest.getInstance("MD5");
      BASE64Encoder base64en = new BASE64Encoder();
      //加密后的字符串
      String newPassword=base64en.encode(md5.digest("123456".getBytes("utf-8")));
      System.out.println("密码"+newPassword+"密码");
    }catch (Exception e){
      e.printStackTrace();
      System.out.println();
    }
  }
//    @Test
//    public void contextLoads() {
//         //url中的  appid 和  secret 开发者会给你  这相当于你小程序的ID和密码       js_code 也会给你  js_code是用微信开发者工具调用方法获得
//            String  appid="wx1fb288a9dc863b05";//你小程序Id
//            String secret="26dc028172cffbdecb553383508a0895";//填入你小程序的secret
//            String code="061wHUG61mnkqM1oJGF617nSG61wHUGE";//用微信开发者工具获取到的code
//            String url="https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+secret+"&js_code="+code+"&grant_type=authorization_code";
//            JSONObject jsonObj= HttpClientUtils.httpGet(url);
//            System.out.println(jsonObj);
//            //打印结果
//
//    }
//    @Autowired(required = false)
//    TbAskReplyMapper tbAskReplyMapper;
//    @Autowired(required = false)
//    TbPostMapper tbPostMapper;
//    @Autowired(required = false)
//    TbCommentMapper tbCommentMapper;
  @Autowired(required = false)
  private TbAskReplyMapper tbAskReplyMapper;

    @Autowired(required = false)
    private TbCommentMapper tbCommentMapper;

    @Autowired(required = false)
    private TbPostMapper tbPostMapper;

    //postDeatilVo（类，collectPostDeatailVo只是多了个state）
    CollectPostDetailVo collectPostDetailVo;

    //评论表，每个评论表含提问和回复
    List<TbCommentsVo> CommentsVoList=null;
    List<TbCommentsVo>  tbCommentsVoList=null;
    //评论表对象
    TbCommentsVo tbCommentsVo=new TbCommentsVo();
//    @Test
//    public void test(){
//            //根据open_id去TbAskReply找到post的先后顺序，返回TbAskReply
//
//        TbComment tbComment=new TbComment();
//        List<MyAskReplyVo> myAskReplyVoList = new ArrayList<MyAskReplyVo>();
//        List<TbAskReply> askReplyList = tbAskReplyMapper.getAskReplyByOpenId("2");
//        tbComment.setFromId("1");
//        for(TbAskReply tbAskReply:askReplyList){
//            tbCommentsVoList=new ArrayList<TbCommentsVo>();
//            tbComment.setPostId(tbAskReply.getPostId());
//            CommentsVoList = tbCommentMapper.selectTbCommentList(tbComment);
//            int index=0;
//            while(CommentsVoList.size()>index) {
//                tbComment.setReplyId(CommentsVoList.get(index).getCommentId());
//                tbComment.setToId(CommentsVoList.get(index).getFromId());
//                tbCommentsVo.setCommentList(tbCommentMapper.selectCommentList(tbComment));
//
//                CommentsVoList.get(index).setCommentList(tbCommentsVo.getCommentList());
//                System.out.println(CommentsVoList.get(index).getFromId());
//
//                    tbCommentsVoList.add(CommentsVoList.get(index));
//
//
//
//                index++;
//            }
//            collectPostDetailVo=tbPostMapper.getPostDetailById(tbAskReply.getPostId());
//            MyAskReplyVo myAskReplyVo =new MyAskReplyVo();
//            myAskReplyVo.setPostDetailList(collectPostDetailVo);
//            myAskReplyVo.setCommentList(tbCommentsVoList);
//            myAskReplyVoList.add(myAskReplyVo);
//    }}

  @Test
  public void test1(){
    TbComment tbComment = new TbComment();
    List<MyAskReplyVo> myAskReplyVoList = new ArrayList<MyAskReplyVo>();
    List<TbAskReply> askReplyList = tbAskReplyMapper.getAskReplyByOpenId("oO5mm5G_GRsyvYOAT1Uc_UfMnmuA");
    for(TbAskReply tb :askReplyList)
      System.out.println(tb.toString());
    System.out.println();
    tbComment.setFromId("oO5mm5G_GRsyvYOAT1Uc_UfMnmuA");
    for (TbAskReply tbAskReply : askReplyList) {
      tbCommentsVoList = new ArrayList<TbCommentsVo>();
      //
      System.out.println();
      tbComment.setPostId(tbAskReply.getPostId());
      CommentsVoList = tbCommentMapper.selectTbCommentList(tbComment);
      int index = 0;
      while (CommentsVoList.size() > index) {
        tbComment.setReplyId(CommentsVoList.get(index).getCommentId());
        tbComment.setToId("oO5mm5G_GRsyvYOAT1Uc_UfMnmuA");
        tbCommentsVo.setCommentList(tbCommentMapper.selectCommentList(tbComment));
        CommentsVoList.get(index).setCommentList(tbCommentsVo.getCommentList());
        if (CommentsVoList.get(index).getCommentList().size() > 0)
          tbCommentsVoList.add(CommentsVoList.get(index));
        index++;
      }
      collectPostDetailVo = tbPostMapper.getPostDetailById(tbAskReply.getPostId());
      MyAskReplyVo myAskReplyVo = new MyAskReplyVo();
      myAskReplyVo.setPostDetailList(collectPostDetailVo);
      myAskReplyVo.setCommentList(tbCommentsVoList);
      if (myAskReplyVo.getCommentList() != null & myAskReplyVo.getCommentList().size() != 0)
        myAskReplyVoList.add(myAskReplyVo);
    }

    System.out.println(myAskReplyVoList);
  }
}