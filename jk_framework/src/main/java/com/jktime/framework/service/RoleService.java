package com.jktime.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jktime.framework.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-04-17 16:34:52
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}

