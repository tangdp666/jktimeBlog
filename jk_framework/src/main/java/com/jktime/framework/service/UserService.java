package com.jktime.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-03-08 16:39:38
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updataUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getSysUserList(Integer pageNum, Integer pageSize, String username, String nickName);

    ResponseResult deleteSysUser(Long[] idList);
}

