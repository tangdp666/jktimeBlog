package com.jktime.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jktime.framework.constants.SystemConstants;
import com.jktime.framework.entity.Comment;
import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.entity.User;
import com.jktime.framework.entity.vo.CommentVo;
import com.jktime.framework.entity.vo.PageVo;
import com.jktime.framework.entity.vo.SysCommentVo;
import com.jktime.framework.enums.AppHttpCodeEnum;
import com.jktime.framework.exception.SystemException;
import com.jktime.framework.mapper.CommentMapper;
import com.jktime.framework.service.CommentService;
import com.jktime.framework.service.UserService;
import com.jktime.framework.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.naming.spi.ResolveResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-03-07 19:34:10
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentMapper commentMapper;



    @Override
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //对articleId进行判断
        queryWrapper.eq(Comment::getArticleId,articleId);
        System.out.println("获取评论1："+queryWrapper.toString());
        //根评论 rootId为-1
        queryWrapper.eq(Comment::getRootId, SystemConstants.COMMENT_NOROOTID);
        System.out.println("获取评论2："+queryWrapper.toString());
        //分页查询
        Page<Comment> page = new Page(pageNum,pageSize);
        page(page, queryWrapper);
        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());

        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        commentVoList.forEach(commentVo -> commentVo.setChildren(getChildren(commentVo.getId())));
        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    private List<CommentVo> getChildren(Long id){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //根评论查询
        queryWrapper.eq(Comment::getRootId,id);
        //创建时间排序
        queryWrapper.orderByDesc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);
        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }

    private List<CommentVo> toCommentVoList(List<Comment> list){
        //遍历vo集合
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list,CommentVo.class);
        //通过creatyBy查询用户的昵称并赋值
        commentVos.forEach(commentVo ->
                           commentVo.setNickName(
                                   userService.getById(commentVo.getCreateBy()).getNickName()
                           ));
        //如果toCommentUserId不为-1才进行查询
        List<CommentVo> commentVos1  =  commentVos.stream()
                  .filter(commentVo -> commentVo.getToCommentUserId() > -1)
                  .collect(Collectors.toList());
        //通过toCommentUserId查询用户的昵称并赋值
        System.out.println(commentVos1);
        if (commentVos1 != null && commentVos1.size() > 0){
                    commentVos.forEach(commentVo ->
                           commentVo.setToCommentUserName(
                                   userService.getById(commentVo.getToCommentUserId()).getNickName()
                           ));
        }

        return commentVos;
    }

    //添加评论
    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不为空
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getSysCommentList(Integer pageNum,Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getDelFlag,SystemConstants.ARTICLE_IS_NODELETE);
        queryWrapper.orderByDesc(Comment::getCreateTime);

        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<SysCommentVo> commentVos = BeanCopyUtils.copyBeanList(page.getRecords(),SysCommentVo.class);
        PageVo pageVo = new PageVo(commentVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult deleteSysComment(Long[] idList) {
          List<Long> commentId = new ArrayList<>();
          commentId.addAll(getRootID(idList));
          commentId.addAll(getToCommentId(idList));
          commentId.addAll(Arrays.asList(idList));
          List<Long> longs = commentId.stream()
                .distinct()
                .collect(Collectors.toList());

        //判断是否为根评论
        //如果是，则删除所有rootId == id 的评论
        //否则
        //判断是否有子节点
        //如果有，则删除所有toCommentId == id的评论
        //否则
        //删除id

        //id去重
        commentMapper.deleteBatchIds(longs);
        return ResponseResult.okResult();
    }


    //查找根评论下的所有评论
    private List<Long> getRootID(Long[] idList){
        //查询所有子评论
        LambdaQueryWrapper<Comment> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.in(Comment::getRootId,idList);
        List<Comment> comments2 = list(queryWrapper2);
        List<Long> ids2 = comments2.stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
        return ids2;
    }

    private List<Long> getToCommentId(Long[] idList){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Comment::getToCommentId,idList);
        List<Comment> comments = list(queryWrapper);
        List<Long> ids = comments.stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
        return ids;
    }


}

