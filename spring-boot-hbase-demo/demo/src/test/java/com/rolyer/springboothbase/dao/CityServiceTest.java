package com.rolyer.springboothbase.dao;

import com.rolyer.springboothbase.entity.City;
import com.rolyer.springboothbase.service.CityService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @ClassName: CityRepositoryTest
 * @Package com.rolyer.springboothbase.dao
 * @Description: ${TOTO} (用一句话描述该文件做什么)
 * @Author rolyer
 * @Date 2019/7/31 16:10
 * @Version V1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CityServiceTest {

    @Autowired
    private CityService cityService;

    @Test
    public void find_all(){
        List<City> cities = cityService.searchAll("city", "info", City.class);

        Assert.assertEquals(6, cities.size());
    }
}