package com.jktime.admin.controller;

import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.service.ArticleService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {


    @Autowired
    private ArticleService articleService;

    @GetMapping("/list")
    public ResponseResult getSysArticleList(Integer pageNum, Integer pageSize, Long categoryId,String title, String summary){
        return articleService.getSysArticleList(pageNum, pageSize, categoryId, title, summary);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteSysArticle(@PathVariable("id") Long id){
        return articleService.deleteArticle(id);
    }
}
