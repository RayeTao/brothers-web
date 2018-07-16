package com.taoran.brothers.media.dao;

import com.taoran.brothers.media.pojo.Media;
import com.taoran.brothers.user.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by taoran
 * date: 2018-07-05 17:55
 */
public interface MediaDAO extends JpaRepository<Media,Integer> {

    public Media save(Media media);

    public Page<Media> findByUserId(int userId, Pageable pageable);

    public List<Media> findByUserId(int userId);
}
