package com.jktime.framework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jktime.framework.entity.Article;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
