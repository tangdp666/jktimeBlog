package com.jktime.framework.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jktime.framework.constants.SystemConstants;
import com.jktime.framework.entity.Article;
import com.jktime.framework.entity.Category;
import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.entity.vo.*;
import com.jktime.framework.mapper.ArticleMapper;
import com.jktime.framework.service.ArticleService;
import com.jktime.framework.service.CategoryService;
import com.jktime.framework.utils.BeanCopyUtils;
import com.jktime.framework.utils.RedisCache;
import com.jktime.framework.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public ResponseResult<Article> hotArticleList() {
        //查询热门文章，封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按浏览量排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询10条
        Page<Article> page = new Page(1,10);
        page(page,queryWrapper);

        List<Article> articles = page.getRecords();

        //bean拷贝
//        List<HotArticleVo> articleVos = new ArrayList<>();
//        for(Article article : articles){
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article, vo);
//            articleVos.add(vo);
//        }

        List<HotArticleVo> vs = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(vs);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //如果有categoryId就要 查询时要和传入的相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        //状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        //对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);
        List<Article> articles = page.getRecords();
        //查询categoryName
//        articles.stream()
//                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
//                .collect(Collectors.toList());
        articles = articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //转换成vo
//        ArticleListVo articleListVo = BeanCopyUtils.copyBean(article, ArticleListVo.class);
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
//        Long categoryId = articleListVo.getCategoryId();
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);

        if (category != null){
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //根据redis中对应 id的浏览量
        redisCache.incrementCacheMapValue("article:viewCount", id.toString(),1);
        System.out.println("浏览量"+redisCache.getCacheMap("article:viewCount"));
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addArticle(ArticleAddVo articleAddVo) {
        //添加博客
        Article article = BeanCopyUtils.copyBean(articleAddVo, Article.class);
        save(article);
        return ResponseResult.okResult();
    }


    //个人信息展示
    @Override
    public ResponseResult userBlog(String status) {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getCreateBy, userId);
        //显示未删除的文章
        queryWrapper.eq(Article::getDelFlag,SystemConstants.ARTICLE_IS_NODELETE);
        //根据状态分别查询
        queryWrapper.eq(Article::getStatus,status);
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        List<ArticleUserVo> articleUserVo = BeanCopyUtils.copyBeanList(articleList, ArticleUserVo.class);
        //返回博客信息
        return ResponseResult.okResult(articleUserVo);
    }

    @Override
    public ResponseResult deleteArticle(Long id) {
        articleMapper.deleteById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateArticle(ArticleAddVo articleAddVo) {
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        //根据id查文章
        updateWrapper.eq(Article::getId,articleAddVo.getId());
        updateWrapper.set(Article::getTitle,articleAddVo.getTitle());
        updateWrapper.set(Article::getContent,articleAddVo.getContent());
        updateWrapper.set(Article::getSummary,articleAddVo.getSummary());
        updateWrapper.set(Article::getCategoryId,articleAddVo.getCategoryId());
        updateWrapper.set(Article::getThumbnail,articleAddVo.getThumbnail());

        //修改
        update(null,updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getSysArticleList(Integer pageNum, Integer pageSize, Long categoryId, String title, String summary) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0,Article::getCategoryId,categoryId);
        queryWrapper.like(Objects.nonNull(title) && title != "",Article::getTitle, title);
//        queryWrapper.like(Article::getTitle, title);
        queryWrapper.like(Objects.nonNull(summary) && summary != "",Article::getSummary,summary);
        //正式发布
        queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        //时间降序
        queryWrapper.orderByDesc(Article::getCreateTime);

        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
//        List<Article> articles = page.getRecords();

        //封装
        List<ArticleDetailVo> articleDetailVos = BeanCopyUtils.copyBeanList(page.getRecords(),ArticleDetailVo.class);
        PageVo pageVo = new PageVo(articleDetailVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }



}


