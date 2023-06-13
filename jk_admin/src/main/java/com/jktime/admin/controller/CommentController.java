package com.jktime.admin.controller;


import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/getCommentList")
    public ResponseResult getSysCommentList(Integer pageNum, Integer pageSize ){
        return commentService.getSysCommentList(pageNum,pageSize);
    }

    @DeleteMapping("/delete")
    public ResponseResult deleteSysComment(Long[] idList){
        return commentService.deleteSysComment(idList);
    }
}
