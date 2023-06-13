package com.jktime.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jktime.framework.entity.Article;
import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.entity.vo.ArticleAddVo;

public interface ArticleService extends IService<Article> {
    ResponseResult<Article> hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(ArticleAddVo articleAddVo);

    ResponseResult userBlog(String status);

    ResponseResult deleteArticle(Long id);

    ResponseResult updateArticle(ArticleAddVo articleAddVo);

    ResponseResult getSysArticleList(Integer pageNum, Integer pageSize, Long categoryId, String title, String summary);

//    ResponseResult deleteSysArticle(Long id);
}
