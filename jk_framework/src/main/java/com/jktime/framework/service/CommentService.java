package com.jktime.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jktime.framework.entity.Comment;
import com.jktime.framework.entity.ResponseResult;

import javax.naming.spi.ResolveResult;
import java.util.List;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-03-07 19:34:10
 */
public interface CommentService extends IService<Comment> {

     ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) ;

     ResponseResult addComment(Comment comment);

     ResponseResult getSysCommentList(Integer pageNum,Integer pageSize);

     ResponseResult deleteSysComment(Long[] idList);
}

