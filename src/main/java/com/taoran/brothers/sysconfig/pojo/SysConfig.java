package com.taoran.brothers.sysconfig.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by taoran
 * date: 2018-07-11 20:01
 */
@Entity
@Getter
@Setter
@ToString
public class SysConfig {
    @Id
    @GeneratedValue
    private int configId;
    private String configName;
    private String configValue;
    private String displayName;
    private String remark;

}
