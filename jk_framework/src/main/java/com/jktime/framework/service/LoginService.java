package com.jktime.framework.service;

import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();

    ResponseResult getInfo();

    ResponseResult getRouters();
}
