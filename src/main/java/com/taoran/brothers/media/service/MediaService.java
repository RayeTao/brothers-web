package com.taoran.brothers.media.service;

import com.taoran.brothers.common.Constant;
import com.taoran.brothers.common.ResultInfo;
import com.taoran.brothers.media.dao.CollectMediaDAO;
import com.taoran.brothers.media.dao.MediaDAO;
import com.taoran.brothers.media.pojo.CollectMedia;
import com.taoran.brothers.media.pojo.Media;
import com.taoran.brothers.sysconfig.dao.SysConfigDAO;
import com.taoran.brothers.sysconfig.pojo.SysConfig;
import com.taoran.brothers.user.dao.UserDAO;
import com.taoran.brothers.user.pojo.User;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by taoran
 * date: 2018-07-05 17:56
 */
@Service
public class MediaService {

    private static final Logger logger = LoggerFactory.getLogger(MediaService.class);

    @Autowired
    MediaDAO mediaDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    SysConfigDAO sysConfigDAO;
    @Autowired
    CollectMediaDAO collectMediaDAO;
    @Autowired
    private EntityManager entityManager;


    public ResultInfo uploadMedia(String type, MultipartFile file) {
        ResultInfo resultInfo = new ResultInfo();
        int userId = 0;
        String userName = "";
        //根据上传类别获取用户信息
        User user = userDAO.findByIndex(type);
        if (user != null) {
            userId = user.getUserId();
            userName = user.getUserName();
        }
        boolean uploadResult = saveFile(userId, userName, file);
        if (uploadResult) {
            resultInfo.setMessage("图片上传成功");
            resultInfo.setSuccess(true);
            resultInfo.setCode(0);
        } else {
            resultInfo.setMessage("图片上传失败");
            resultInfo.setSuccess(false);
        }
        return resultInfo;
    }

    public boolean saveFile(int userId, String userName, MultipartFile file) {

        String basePath = "";
        SysConfig sysConfigPath = sysConfigDAO.findByConfigName(Constant.CONFIG_IMAGE_BASE_FILE_PATH);
        if (sysConfigPath != null) {
            basePath = sysConfigPath.getConfigValue();
        }

        String filePath = userName + "/";
        File mediaFile = new File(basePath + filePath);
        if (!mediaFile.exists()) {
            mediaFile.mkdirs();
        }
        if (!file.isEmpty()) {
            try {
                UUID uuid = UUID.randomUUID();
                String mediaType = ".jpg";
                if (file.getContentType().equals("image/png")) {
                    mediaType = ".png";
                }
                String saveFilePath = basePath + filePath + uuid + mediaType;
                file.transferTo(new File(saveFilePath));

                //图片尺寸不变，压缩图片文件大小
                Thumbnails.of(saveFilePath).scale(1f).outputQuality(0.25f).toFile(saveFilePath);

                //meidia信息保存到数据库中
                Media media = new Media();
                media.setMediaName(file.getOriginalFilename());
                media.setMediaType(file.getContentType());
                media.setSize(file.getSize());
                media.setUserId(userId);
                media.setLocalMediaPath(saveFilePath);

                //获取图片服务器的地址
                SysConfig sysConfigUrl = sysConfigDAO.findByConfigName(Constant.CONFIG_IMAGE_BASE_URL);
                if (sysConfigUrl != null) {
                    media.setMediaUrl(sysConfigUrl.getConfigValue() + filePath + uuid + mediaType);
                }
                mediaDAO.save(media);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public ResultInfo getMediaList(String userType, int pageNo, int pageSize) {
        ResultInfo resultInfo = new ResultInfo();
        User user = userDAO.findByIndex(userType);
        if (user != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            Pageable pageable = new PageRequest(pageNo - 1, pageSize);
            Page<Media> page = mediaDAO.findByUserId(user.getUserId(), pageable);
            if (page != null) {
                List<Media> list = page.getContent();
                if (list != null && list.size() > 0) {
                    resultInfo.setCode(0);
                    resultInfo.setSuccess(true);
                    map.put("resultList", list);
                    resultInfo.setData(map);
                } else {
                    resultInfo.setSuccess(false);
                }
            }
            List<Media> listCount = mediaDAO.findByUserId(user.getUserId());
            if (listCount != null && listCount.size() > 0) {
                map.put("totalCount", listCount.size());
            }
        }
        return resultInfo;
    }

    public ResultInfo downloadMedia(String mediaUrl) {
        ResultInfo resultInfo = new ResultInfo();
        try {
        } catch (Exception e) {
        }
        return resultInfo;
    }

    @Transactional
    public ResultInfo deleteMedia(int mediaId, String localMediaPath) {
        ResultInfo resultInfo = new ResultInfo();
        try {
            int result = mediaDAO.deleteByMediaId(mediaId);
            if (result == 1) {
                File file = new File(localMediaPath);
                file.delete();
                resultInfo.setSuccess(true);
                resultInfo.setMessage("图片删除成功!");
            }
            return resultInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultInfo;
    }

    public ResultInfo collectMedia(int mediaId,int userId){
        ResultInfo resultInfo = new ResultInfo();
        CollectMedia collectMedia = new CollectMedia();
        collectMedia.setMediaId(mediaId);
        collectMedia.setUserId(userId);
        collectMedia.setCreateTime(new Date());
        CollectMedia result = collectMediaDAO.save(collectMedia);
        if(result != null){
            resultInfo.setSuccess(true);
            resultInfo.setMessage("收藏成功");
        }
        return resultInfo;
    }

    public ResultInfo getCollectMediaList(int userId,int pageNo,int pageSize) {
        ResultInfo resultInfo = new ResultInfo();

        Query query = this.entityManager.createQuery("SELECT m from Media m,CollectMedia cm where m.mediaId = cm.mediaId and cm.userId=:userId")
                .setParameter("userId",userId).setMaxResults(pageSize).setFirstResult(pageNo-1);

        List<Media> list = query.getResultList();

        if(list != null && list.size()>0){
            for(Media media : list){
                long count = collectMediaDAO.countByMediaId(media.getMediaId());
                media.setCollectCount(count);
                media.setCollectFlag(1);
            }
            Map<String,Object> map = new HashMap<>();
            map.put("resultList", list);
            map.put("totalCount", list.size());
            resultInfo.setData(map);
            resultInfo.setSuccess(true);
        }

        return resultInfo;
    }
}
