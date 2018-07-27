package com.taoran.brothers.media.service;

import com.taoran.brothers.common.Constant;
import com.taoran.brothers.common.ResultInfo;
import com.taoran.brothers.media.dao.CollectMediaDAO;
import com.taoran.brothers.media.dao.CommentDAO;
import com.taoran.brothers.media.dao.MediaDAO;
import com.taoran.brothers.media.pojo.CollectMedia;
import com.taoran.brothers.media.pojo.Comment;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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
    @Autowired
    private CommentDAO commentDAO;


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
        String thumbBasePath = "";
        SysConfig sysConfigPath = sysConfigDAO.findByConfigName(Constant.CONFIG_IMAGE_BASE_FILE_PATH);
        if (sysConfigPath != null) {
            basePath = sysConfigPath.getConfigValue();
        }
        SysConfig sysConfigThumbPath = sysConfigDAO.findByConfigName(Constant.CONFIG_IMAGE_BASE_THUMB_FILE_PATH);
        if(sysConfigThumbPath != null){
            thumbBasePath = sysConfigThumbPath.getConfigValue();
        }

        String filePath = userName + "/";
        File mediaFile = new File(basePath + filePath);
        if (!mediaFile.exists()) {
            mediaFile.mkdirs();
        }
        File thumbMediaFile = new File(thumbBasePath + filePath);
        if(!thumbMediaFile.exists()){
            thumbMediaFile.mkdirs();
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
                //压缩图片存放路径下
                String saveThumbPath = thumbBasePath + filePath + uuid + mediaType;
                Thumbnails.of(saveFilePath).scale(1f).outputQuality(0.25f).toFile(saveThumbPath);

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
                    media.setThumbMediaUrl(sysConfigUrl.getConfigValue()+"thumb/" + filePath + uuid + mediaType);
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

    public ResultInfo getMediaList(int loginUserId,String userType, int pageNo, int pageSize) {
        ResultInfo resultInfo = new ResultInfo();
        User user = userDAO.findByIndex(userType);
        if (user != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            Pageable pageable = new PageRequest(pageNo - 1, pageSize);
            Page<Media> page = mediaDAO.findByUserId(user.getUserId(), pageable);
            if (page != null) {
                List<Media> list = page.getContent();
                if (list != null && list.size() > 0) {
                    for(Media media : list){
                        long count = collectMediaDAO.countByMediaId(media.getMediaId());
                        media.setCollectCount(count);
                        CollectMedia collectMedia = collectMediaDAO.findByMediaIdAndUserId(media.getMediaId(),loginUserId);
                        if(collectMedia == null){
                            media.setCollectFlag(0);
                        }else{
                            media.setCollectFlag(1);
                        }
                    }
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

    public ResultInfo downloadMedia(String filePath,HttpServletResponse response) {
        ResultInfo resultInfo = new ResultInfo();
        try {
            File f = new File("E://git//brothers//MediaRoot//images//陶然//10451281-8500-4c0d-a2b5-2a8359b64ee4.png");
            if (!f.exists()) {
                response.sendError(404, "File not found!");
                return resultInfo;
            }
            BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
            byte[] buf = new byte[1024];
            int len = 0;
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + "1.png");
            OutputStream out = response.getOutputStream();
            while ((len = br.read(buf)) > 0) out.write(buf, 0, len);
            br.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

    @Transactional
    public ResultInfo cancelCollectMedia(int mediaId) {
        ResultInfo resultInfo = new ResultInfo();
        int result = collectMediaDAO.deleteByMediaId(mediaId);
        if(result == 1){
            resultInfo.setSuccess(true);
            resultInfo.setMessage("取消收藏成功");
        }
        return resultInfo;
    }

    public ResultInfo commentMedia(int userId, int mediaId, String content) {
        ResultInfo resultInfo = new ResultInfo();
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setMediaId(mediaId);
        comment.setContent(content);
        comment.setCreateTime(new Date());
        Comment result = commentDAO.save(comment);
        if(result != null){
            resultInfo.setSuccess(true);
            resultInfo.setMessage("评论成功");
        }
        return resultInfo;
    }

    public ResultInfo  getCommentList(int mediaId,int pageNo,int pageSize){
        ResultInfo resultInfo = new ResultInfo();
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        Pageable pageable = new PageRequest(pageNo-1,pageSize,sort);

        Page<Comment> commentPage = commentDAO.findByMediaId(mediaId,pageable);

        if(commentPage != null){
            List<Comment> list = commentPage.getContent();
            for(Comment comment : list) {
                User user = userDAO.findOne(comment.getUserId());
                comment.setUserName(user.getUserName());
            }
            Map<String,Object> map = new HashMap<>();
            map.put("resultList",commentPage.getContent());
            resultInfo.setSuccess(true);
            resultInfo.setData(map);

            long count = commentDAO.countByMediaId(mediaId);
            map.put("totalCount", count);
        }
        return  resultInfo;
    }
}
