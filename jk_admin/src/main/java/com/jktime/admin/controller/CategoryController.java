package com.jktime.admin.controller;


import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseResult getSysCategoryList(Integer pageNum, Integer pageSize){
        return categoryService.getSysCategoryList(pageNum, pageSize);
    }

    @DeleteMapping("/delete")
    public ResponseResult deleteSysCategory(Long[] idList){
        return categoryService.deleteSysCategory(idList);
    }

    @PostMapping("/addCategory")
    public ResponseResult addCategory(String name){
        return categoryService.addCategory(name);
    }
}
