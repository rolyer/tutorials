package com.rolyer.springboothbase.service.impl;

import com.rolyer.springboothbase.dao.CityRepository;
import com.rolyer.springboothbase.entity.City;
import com.rolyer.springboothbase.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: CityServiceImpl
 * @Package com.rolyer.springboothbase.service.impl
 * @Description: ${TOTO} (用一句话描述该文件做什么)
 * @Author rolyer
 * @Date 2019/7/30 15:25
 * @Version V1.0
 */
@Service
public class CityServiceImpl implements CityService {
    @Autowired
    private CityRepository cityRepository;

    @Override
    public City save(City city) {
        city.setCreated(System.currentTimeMillis());
        city.setUpdated(System.currentTimeMillis());
        return cityRepository.save(city);
    }

    @Override
    public boolean delete(String code) {
        return cityRepository.delete(code);
    }

    @Override
    public City update(City city) {
        city.setUpdated(System.currentTimeMillis());
        return cityRepository.update(city);
    }


    @Override
    public List<City> list() {
        return cityRepository.findAll();
    }
}
