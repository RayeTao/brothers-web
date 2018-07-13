package com.taoran.brothers.media.controller;

import com.taoran.brothers.common.ResultInfo;
import com.taoran.brothers.media.service.MediaService;
import com.taoran.brothers.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;



/**
 * Created by taoran
 * date: 2018-07-05 15:47
 */
@RestController
@RequestMapping(value = "/media")
public class MediaController {
    private static final Logger logger = LoggerFactory.getLogger(MediaController.class);

    @Autowired
    MediaService mediaService;

    @Autowired
    UserService userService;

    /**
     * 图片上传
     * @param type
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public ResultInfo uploadMedia(@RequestParam(required = true)String type,@RequestParam(required = true) MultipartFile file){
        return mediaService.uploadMedia(type,file);
    }



    /**
     * 获取多媒体列表
     * @param userType
     * @return
     */
    @RequestMapping(value = "/getMediaList",method = RequestMethod.GET)
    public ResultInfo getMediaList(@RequestParam(required = true) String  userType){
       return mediaService.getMediaList(userType);
    }

    /**
     * 下载多媒体
     * @param mediaUrl
     * @return
     */
    @RequestMapping(value = "download",method = RequestMethod.GET)
    public ResultInfo downloadMedia(@RequestParam("mediaUrl") String  mediaUrl, @RequestParam("size") int size, HttpServletResponse response) {
        return mediaService.downloadMedia(mediaUrl, size, response);
    }
}
