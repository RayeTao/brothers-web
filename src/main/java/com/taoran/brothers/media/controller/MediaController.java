package com.taoran.brothers.media.controller;

import com.taoran.brothers.common.ResultInfo;
import com.taoran.brothers.media.pojo.Media;
import com.taoran.brothers.media.service.MediaService;
import com.taoran.brothers.user.pojo.User;
import com.taoran.brothers.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;


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
    public ResultInfo uploadMedia(@RequestParam("type") String type,@RequestParam("file") MultipartFile file){

        ResultInfo resultInfo = new ResultInfo();
         int userId = 0;
         String userName = "";
        //根据上传类别获取用户信息
        User user = userService.findByIndex(type);
        if(user!= null){
            userId = user.getUserId();
            userName = user.getUserName();
        }
        boolean uploadResult = saveFile(userId,userName,file);
        if(uploadResult){
            resultInfo.setMessage("图片上传成功");
            resultInfo.setSuccess(true);
            resultInfo.setCode(0);
        }


        return resultInfo;
    }

    public boolean saveFile(int userId,String userName,MultipartFile file){
        logger.info("存放目录："+userName);
        //String filePath = "E:/images/"+userName+"/";   //windows路径
        String filePath = "/apps/MediaRoot/images/"+userName+"/"; //linux路径
        File mediaFile = new File(filePath);
        if(!mediaFile.exists()){
            mediaFile.mkdirs();
        }
        if(!file.isEmpty()){
            try {
                UUID uuid = UUID.randomUUID();
                String mediaType=".jpg";
                if(file.getContentType().equals("image/png")){
                    mediaType=".png";
                }
                String saveFilePath = filePath + uuid + mediaType;
                file.transferTo(new File(saveFilePath));

                //meidia信息保存到数据库中
                Media media = new Media();
                media.setMediaName(file.getOriginalFilename());
                media.setMediaType(file.getContentType());
                media.setSize(file.getSize());
                media.setUserId(userId);
                media.setMediaUrl(saveFilePath);
                mediaService.save(media);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
