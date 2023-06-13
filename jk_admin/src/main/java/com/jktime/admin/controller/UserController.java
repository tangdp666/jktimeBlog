package com.jktime.admin.controller;


import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult getSysUserList(Integer pageNum, Integer pageSize, String username, String nickName){
        return userService.getSysUserList(pageNum, pageSize, username, nickName);
    }

    @DeleteMapping("/delete")
    public ResponseResult deleteSysUser(Long[] idList){
        return userService.deleteSysUser(idList);
    }

}
