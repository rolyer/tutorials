package com.rolyer.springboothbase.service;

import com.rolyer.springboothbase.entity.City;

import java.util.List;

/**
 * @ClassName: CityService
 * @Package com.rolyer.springboothbase.service
 * @Description: ${TOTO} (用一句话描述该文件做什么)
 * @Author rolyer
 * @Date 2019/7/30 15:22
 * @Version V1.0
 */
public interface CityService {
    /**
     * 保存
     * @param city
     * @return
     */
    City save(City city);

    boolean delete(String code);

    City update(City city);

    /**
     * 获取所有记录集
     * @return
     */
    List<City> list();

}
