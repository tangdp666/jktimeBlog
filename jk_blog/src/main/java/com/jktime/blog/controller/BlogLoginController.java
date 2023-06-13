package com.jktime.blog.controller;


import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.entity.User;
import com.jktime.framework.enums.AppHttpCodeEnum;
import com.jktime.framework.exception.SystemException;
import com.jktime.framework.service.BlogLoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api")
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    //用户登录
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    //退出登录
    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
