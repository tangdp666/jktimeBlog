package com.jktime.blog.controller;


import com.jktime.framework.annotation.SysteLog;
import com.jktime.framework.entity.Article;
import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.entity.vo.ArticleAddVo;
import com.jktime.framework.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController

@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    //热门文章查询
    @GetMapping("/hotArticleList")
    @SysteLog(businessName = "热门文章查询")
    public ResponseResult<Article> hotArticleList(){

       ResponseResult<Article> result =  articleService.hotArticleList();
       return result;
    }
    //总文章查询
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId){
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    //详细文章查询
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }


    //增加浏览量
    @PutMapping("updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){

        return articleService.updateViewCount(id);
    }

    //新增文章
    @PostMapping("/addArticle")
    public ResponseResult addArticle(@RequestBody ArticleAddVo articleAddVo){
        return articleService.addArticle(articleAddVo);
    }

    //个人正式文章展示
    @GetMapping("/userBlog/{status}")
    public ResponseResult userBlog(@PathVariable("status") String status){
        return articleService.userBlog(status);
    }

    //删除文章
    @DeleteMapping("/deleteArticle/{id}")
    public ResponseResult deleteArticle(@PathVariable("id") Long id){
        return articleService.deleteArticle(id);
    }

    //修改文章
    @PutMapping("/updateArticle")
    public ResponseResult updateArticle(@RequestBody ArticleAddVo articleAddVo){
        return articleService.updateArticle(articleAddVo);
    }
}
