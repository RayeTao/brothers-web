package com.taoran.brothers.media.controller;

import com.sun.deploy.net.HttpResponse;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        }else{
            resultInfo.setMessage("图片上传失败");
            resultInfo.setSuccess(false);
        }


        return resultInfo;
    }

    public boolean saveFile(int userId,String userName,MultipartFile file){
        String filePath = userName+"/";   //windows路径
        //String filePath = "/apps/MediaRoot/images/"+userName+"/"; //linux路径
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

                media.setMediaUrl("http://localhost/images/"+saveFilePath);
                mediaService.save(media);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 获取多媒体列表
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getMediaList",method = RequestMethod.GET)
    public ResultInfo getMediaList(@RequestParam("userId") int userId){
        ResultInfo resultInfo = new ResultInfo();
        List<Media> list = mediaService.findByUserId(userId);
        if(list != null && list.size()>0){
            resultInfo.setCode(0);
            resultInfo.setSuccess(true);
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("resultList",list);
            resultInfo.setData(map);
        }else{
            resultInfo.setSuccess(false);
        }
        return resultInfo;
    }

    /**
     * 下载多媒体
     * @param mediaUrl
     * @return
     */
    @RequestMapping(value = "download",method = RequestMethod.GET)
    public ResultInfo downloadMedia(@RequestParam("mediaUrl") String  mediaUrl, @RequestParam("size") int size, HttpServletResponse response){
        ResultInfo resultInfo = new ResultInfo();
        try{
            File file = new File(mediaUrl);
            OutputStream out = response.getOutputStream();
            FileInputStream in = new FileInputStream(file);
            byte[] buf = new byte[size];
            int len = -1;
            while((len=in.read(buf))!=-1){
                out.write(buf,0,len);
            }
            in.close();
            out.close();
            Map<String,Object> map = new HashMap<>();
            map.put("result",buf);
            resultInfo.setSuccess(true);
            resultInfo.setData(map);

        }catch (Exception e){
            resultInfo.setSuccess(false);
        }
        return resultInfo;
    }
}
