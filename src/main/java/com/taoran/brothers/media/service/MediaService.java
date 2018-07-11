package com.taoran.brothers.media.service;

import com.taoran.brothers.media.dao.MediaDAO;
import com.taoran.brothers.media.pojo.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by taoran
 * date: 2018-07-05 17:56
 */
@Service
public class MediaService {

    @Autowired
    MediaDAO mediaDAO;

    public Media save(Media media){
        return mediaDAO.save(media);
    }


    public List<Media> findByUserId(int userId) {
       return mediaDAO.findByUserId(userId);
    }
}
