package com.jktime.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jktime.framework.entity.Category;
import com.jktime.framework.entity.ResponseResult;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-03-07 19:23:28
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult getAllCategoryList();

    ResponseResult getSysCategoryList(Integer pageNum, Integer pageSize);

    ResponseResult deleteSysCategory(Long[] idList);

    ResponseResult addCategory(String name);
}

