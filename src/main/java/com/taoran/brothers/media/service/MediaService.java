package com.taoran.brothers.media.service;

import com.taoran.brothers.common.Constant;
import com.taoran.brothers.common.ResultInfo;
import com.taoran.brothers.media.dao.MediaDAO;
import com.taoran.brothers.media.pojo.Media;
import com.taoran.brothers.sysconfig.dao.SysConfigDAO;
import com.taoran.brothers.sysconfig.pojo.SysConfig;
import com.taoran.brothers.user.dao.UserDAO;
import com.taoran.brothers.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by taoran
 * date: 2018-07-05 17:56
 */
@Service
public class MediaService {

    @Autowired
    MediaDAO mediaDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    SysConfigDAO sysConfigDAO;


    public ResultInfo uploadMedia(String type,MultipartFile file){
        ResultInfo resultInfo = new ResultInfo();
        int userId = 0;
        String userName = "";
        //根据上传类别获取用户信息
        User user = userDAO.findByIndex(type);
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

        String basePath = "";
        SysConfig sysConfigPath = sysConfigDAO.findByConfigName(Constant.CONFIG_IMAGE_BASE_FILE_PATH);
        if(sysConfigPath != null ){
            basePath = sysConfigPath.getConfigValue();
        }

        String filePath =  userName+"/";
        File mediaFile = new File(basePath+filePath);
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
                String saveFilePath = basePath + filePath + uuid + mediaType;
                file.transferTo(new File(saveFilePath));

                //meidia信息保存到数据库中
                Media media = new Media();
                media.setMediaName(file.getOriginalFilename());
                media.setMediaType(file.getContentType());
                media.setSize(file.getSize());
                media.setUserId(userId);

                //获取图片服务器的地址
                SysConfig sysConfigUrl = sysConfigDAO.findByConfigName(Constant.CONFIG_IMAGE_BASE_URL);
                if(sysConfigUrl != null){
                    media.setMediaUrl(sysConfigUrl.getConfigValue()+filePath + uuid + mediaType);
                }
                mediaDAO.save(media);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public ResultInfo getMediaList(String userType,int pageNo,int pageSize){
        ResultInfo resultInfo = new ResultInfo();
        User user = userDAO.findByIndex(userType);
        if(user != null ){
            Map<String,Object> map = new HashMap<String,Object>();
            Pageable pageable = new PageRequest(pageNo-1,pageSize);
            Page<Media> page = mediaDAO.findByUserId(user.getUserId(),pageable);
            if(page !=null){
                List<Media> list = page.getContent();
                if(list != null && list.size()>0){
                    resultInfo.setCode(0);
                    resultInfo.setSuccess(true);
                    map.put("resultList",list);
                    resultInfo.setData(map);
                }else{
                    resultInfo.setSuccess(false);
                }
            }

           List<Media> listCount = mediaDAO.findByUserId(user.getUserId());
            if(listCount != null && listCount.size()>0){
               map.put("totalCount",listCount.size()) ;
            }
        }


        return resultInfo;
    }

    public ResultInfo downloadMedia(String mediaUrl, int size, HttpServletResponse response){
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
