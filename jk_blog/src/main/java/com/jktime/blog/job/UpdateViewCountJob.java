package com.jktime.blog.job;


import com.jktime.framework.entity.Article;
import com.jktime.framework.service.ArticleService;
import com.jktime.framework.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;


    @Scheduled(cron = "0 0 0/1 * * ?")
    public void updateViewCount(){
        //获取redis中的浏览量
        Map<String, Integer> cacheMap = redisCache.getCacheMap("article:viewCount");

        List<Article> articles = cacheMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新到数据库
        articleService.updateBatchById(articles);
    }
}
