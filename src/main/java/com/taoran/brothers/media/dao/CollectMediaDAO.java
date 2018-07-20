package com.taoran.brothers.media.dao;

import com.taoran.brothers.media.pojo.CollectMedia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by taoran
 * date: 2018-07-20 11:30
 */
public interface CollectMediaDAO extends JpaRepository<CollectMedia,Integer> {


    public CollectMedia save(CollectMedia collectMedia);

    public Page<CollectMedia> findByUserId(int userId, Pageable pageable);

    public Long countByMediaId(int mediaId);
}
