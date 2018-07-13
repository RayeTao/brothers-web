package com.taoran.brothers.sysconfig.dao;

import com.taoran.brothers.sysconfig.pojo.SysConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by taoran
 * date: 2018-07-11 20:16
 */
public interface SysConfigDAO extends JpaRepository<SysConfig,Integer> {

    public SysConfig findByConfigName(String configName);
}
