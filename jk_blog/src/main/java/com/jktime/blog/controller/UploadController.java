package com.jktime.blog.controller;


import com.jktime.framework.entity.ResponseResult;
import com.jktime.framework.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;


    //上传头像
    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img){
        return uploadService.uploadImg(img);
    }

    //文章上传图片
    @PostMapping("/article/upload")
    public ResponseResult uploadImgArticle(@RequestParam("img") MultipartFile multipartFile){
        return uploadService.uploadImg(multipartFile);
    }
}
