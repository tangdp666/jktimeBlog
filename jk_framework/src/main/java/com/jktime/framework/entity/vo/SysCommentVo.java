package com.jktime.framework.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysCommentVo {

    @TableId
    private Long id;

    //文章id
    private Long articleId;

    //评论内容
    private String content;
    //所回复的目标评论的userid
    private Long toCommentUserId;

    //创建人用户id
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
}
