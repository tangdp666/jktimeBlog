package com.jktime.framework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jktime.framework.entity.Role;
import org.mapstruct.Mapper;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-17 16:34:52
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeysByUserId(Long id);
}

