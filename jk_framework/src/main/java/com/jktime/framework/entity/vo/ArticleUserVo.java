package com.jktime.framework.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleUserVo {

    private Long id;
    //标题
    private String title;
    //文章摘要
    private String summary;

    //所属分类名
    private String categoryName;
    //缩略图
    private String thumbnail;

    private Date createTime;
}
