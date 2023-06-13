package com.jktime.admin.controller;

import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.entity.User;
import com.jktime.framework.enums.AppHttpCodeEnum;
import com.jktime.framework.exception.SystemException;
import com.jktime.framework.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminLoginController {

    @Autowired
    private LoginService loginService;



    //用户登录
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    //登录信息
    @GetMapping("/getInfo")
    public ResponseResult getInfo(){
        return loginService.getInfo();
    }

    //权限路径
    @GetMapping("/getRouters")
    public ResponseResult getRouters(){
        return loginService.getRouters();
    }

    //退出登录
    @PostMapping("/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }


//    @GetMapping("/getTextList")
//    public List<Text> getTextList(){
//        return textMapper.getTextList();
//    }
//
//    @GetMapping("/getUsername")
//    public String getUsername(){
//        return textMapper.getUsername();
//    }
}