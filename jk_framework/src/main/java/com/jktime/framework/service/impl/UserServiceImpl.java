package com.jktime.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jktime.framework.constants.SystemConstants;
import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.entity.User;
import com.jktime.framework.entity.vo.PageVo;
import com.jktime.framework.entity.vo.SysUserVo;
import com.jktime.framework.entity.vo.UserInfoVo;
import com.jktime.framework.mapper.UserMapper;
import com.jktime.framework.service.UserService;
import com.jktime.framework.utils.BeanCopyUtils;
import com.jktime.framework.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-03-08 16:39:38
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfovo
        UserInfoVo vo = BeanCopyUtils.copyBean(user,UserInfoVo.class);

        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updataUserInfo(User user) {

        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        //查询id
        updateWrapper.eq(User::getId,user.getId());
        //修改值
        if (StringUtils.hasText(user.getEmail())){
            updateWrapper.set(User::getEmail,user.getEmail());
        }
        if (StringUtils.hasText(user.getNickName())){
            updateWrapper.set(User::getNickName,user.getNickName());
        }
        if(StringUtils.hasText(user.getSex())){
            updateWrapper.set(User::getSex,user.getSex());
        }

        update(null,updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        //对数据进行重复判断
        //密码加密
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        String nike = user.getEmail().substring(0, user.getEmail().indexOf("@"));
        //默认昵称
        user.setNickName("用户"+nike);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getSysUserList(Integer pageNum, Integer pageSize, String username, String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getDelFlag, SystemConstants.ARTICLE_IS_NODELETE);
        queryWrapper.like(Objects.nonNull(username) && username != "",User::getUserName,username);
        queryWrapper.like(Objects.nonNull(nickName) && nickName != "",User::getNickName,nickName);
        queryWrapper.orderByDesc(User::getCreateTime);

        Page<User> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<SysUserVo> sysUserVoList = BeanCopyUtils.copyBeanList(page.getRecords(),SysUserVo.class);
        PageVo pageVo = new PageVo(sysUserVoList, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult deleteSysUser(Long[] idList) {
        userMapper.deleteBatchIds(Arrays.asList(idList));
        return ResponseResult.okResult();
    }
}

