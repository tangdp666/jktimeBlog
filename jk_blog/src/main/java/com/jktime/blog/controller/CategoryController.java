package com.jktime.blog.controller;


import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    //分类查询
    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList(){
        return categoryService.getCategoryList();
    }

    //所以分类查询
    @GetMapping("/getAllCategoryList")
    public ResponseResult getAllCategoryList(){
        return categoryService.getAllCategoryList();
    }
}
