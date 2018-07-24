package com.taoran.brothers.media.dao;

import com.taoran.brothers.media.pojo.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by taoran
 * date: 2018-07-23 16:14
 */
public interface CommentDAO extends JpaRepository<Comment,Integer> {

    public Comment save(Comment comment);

    public Page<Comment> findByMediaId(int mediaId, Pageable pageable);

    public long countByMediaId(int mediaId);
}
