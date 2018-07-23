package com.taoran.brothers.media.controller;

import com.taoran.brothers.common.ResultInfo;
import com.taoran.brothers.media.pojo.Media;
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
    public ResultInfo getMediaList(@RequestParam int userId,@RequestParam(required = true) String  userType,@RequestParam int pageNo,@RequestParam int pageSize){
       return mediaService.getMediaList(userId,userType,pageNo,pageSize);
    }

    /**
     * 下载多媒体
     * @param mediaUrl
     * @return
     */
    @RequestMapping(value = "/downloadMedia",method = RequestMethod.GET)
    public ResultInfo downloadMedia(@RequestParam("mediaUrl") String  mediaUrl) {
        return mediaService.downloadMedia(mediaUrl);
    }

    /**
     * 删除多媒体
     * @param mediaId
     * @param localMediaPath
     * @return
     */
    @RequestMapping(value = "/deleteMedia",method = RequestMethod.GET)
    public ResultInfo deleteMedia(@RequestParam int mediaId,@RequestParam String localMediaPath){
        return  mediaService.deleteMedia(mediaId,localMediaPath);
    }

    /**
     * 收藏多媒体
     * @param mediaId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/collectMedia",method = RequestMethod.GET)
    public ResultInfo collectMedia(@RequestParam int mediaId,@RequestParam int userId){
        return mediaService.collectMedia(mediaId,userId);
    }

    /**
     * 获取收藏列表
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getCollectMediaList",method = RequestMethod.GET)
    public ResultInfo getCollectMediaList(@RequestParam int userId,@RequestParam int pageNo,@RequestParam int pageSize){
        return mediaService.getCollectMediaList(userId,pageNo,pageSize);
    }

    /**
     * 取消收藏
     * @param mediaId
     * @return
     */
    @RequestMapping(value = "/cancelCollectMedia",method = RequestMethod.GET)
    public ResultInfo cancelCollectMedia(@RequestParam int mediaId){
        return mediaService.cancelCollectMedia(mediaId);
    }

    /**
     * 评论
     * @param userId
     * @param mediaId
     * @param content
     * @return
     */
    @RequestMapping(value = "/commentMedia",method = RequestMethod.GET)
    public ResultInfo commentMedia(@RequestParam int userId,@RequestParam int mediaId,@RequestParam String content){
        return mediaService.commentMedia(userId,mediaId,content);
    }

    /**
     * 获取评论列表
     * @param mediaId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getCommentList",method = RequestMethod.GET)
    public ResultInfo getCommentList(@RequestParam int mediaId,@RequestParam int pageNo,@RequestParam int pageSize){
        return mediaService.getCommentList(mediaId,pageNo,pageSize);
    }
}
