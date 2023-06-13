package com.jktime.framework.service;

import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}