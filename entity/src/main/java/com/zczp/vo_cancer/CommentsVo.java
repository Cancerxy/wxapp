package com.zczp.vo_cancer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
/**
 *@author: cancer
 *@data: 2019/7/26 15:01
 *@descriptions: 提问回复类
 *@version: 1.0
 */
@Data
public class CommentsVo {
    @ApiModelProperty("评论Id")
    private Integer commentId;

    @ApiModelProperty("招聘信息Id")
    private Integer postId;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("用户ID")
    private Integer fromId;

    @ApiModelProperty("用户头像")
    private String userImage;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("评论时间")
    private Date commentTime;

    @ApiModelProperty("回复的评论ID")
    private Integer replyId;

    @ApiModelProperty("回复列表")
    private List<CommentsVo> commentList;

}
