package com.jktime.framework.entity.vo;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserVo {
    //主键
    @TableId
    private Long id;
    //用户名
    private String userName;
    //昵称
    private String nickName;
    //用户类型：0代表普通用户，1代表管理员
    private String type;
    //邮箱
    private String email;
    //用户性别（0男，1女，2未知）
    private String sex;
    //头像
    private String avatar;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
}
