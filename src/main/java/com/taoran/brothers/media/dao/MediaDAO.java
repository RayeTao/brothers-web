package com.taoran.brothers.media.dao;

import com.taoran.brothers.media.pojo.Media;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by taoran
 * date: 2018-07-05 17:55
 */
public interface MediaDAO extends JpaRepository<Media,Integer> {

    public Media save(Media media);
}
