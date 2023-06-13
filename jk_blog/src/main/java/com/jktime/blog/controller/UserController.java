package com.jktime.blog.controller;


import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.entity.User;
import com.jktime.framework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    //个人信息查看
    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    //修改个人信息
    @PutMapping("/userInfo")
    public ResponseResult updataUserInfo(@RequestBody User user){
        return userService.updataUserInfo(user);
    }

    //用户注册
    @PostMapping("/register")
    public ResponseResult  register(@RequestBody User user){
        return userService.register(user);
    }
}
