package com.taoran.brothers.media.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * Created by taoran
 * date: 2018-07-05 18:19
 */
@Entity
@Getter
@Setter
public class Media {
    @Id
    @GeneratedValue
    private int mediaId;
    private int userId;
    private String mediaUrl;
    private String mediaName;
    private String mediaType;
    private Long size;
    private String remark;
    private String localMediaPath;

    @Transient
    private int collectFlag;
    @Transient
    private Long collectCount;

}
