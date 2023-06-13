package com.jktime.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jktime.framework.constants.SystemConstants;
import com.jktime.framework.entity.Article;
import com.jktime.framework.entity.Category;
import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.entity.vo.CategoryVo;
import com.jktime.framework.entity.vo.PageVo;
import com.jktime.framework.mapper.CategoryMapper;
import com.jktime.framework.service.ArticleService;
import com.jktime.framework.service.CategoryService;
import com.jktime.framework.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-03-07 19:23:28
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseResult getCategoryList() {
        //查询文章表，状态为已经发布的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);

        //获取文章的分类id，并且去重
        Set<Long> categoryIds = articleList.stream()
                                           .map(Article::getCategoryId)
                                           .collect(Collectors.toSet());

        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream()
                               .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                               .collect(Collectors.toList());

        //封装VO
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult getAllCategoryList() {
        LambdaQueryWrapper<Category> categoryWrapper = new LambdaQueryWrapper<>();
        categoryWrapper.eq(Category::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        categoryWrapper.eq(Category::getDelFlag, SystemConstants.ARTICLE_STATUS_NORMAL);

        List<Category> categoryList = categoryMapper.selectList(categoryWrapper);
        //封装VO
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categoryList,CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult getSysCategoryList(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getDelFlag,SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc(Category::getCreateTime);

        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<CategoryVo> categoryVoList = BeanCopyUtils.copyBeanList(page.getRecords(),CategoryVo.class);
        PageVo pageVo = new PageVo(categoryVoList, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult deleteSysCategory(Long[] idList) {
        categoryMapper.deleteBatchIds(Arrays.asList(idList));
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addCategory(String name) {
        Category category = new Category();
        category.setName(name);
        save(category);
        return ResponseResult.okResult();
    }
}

