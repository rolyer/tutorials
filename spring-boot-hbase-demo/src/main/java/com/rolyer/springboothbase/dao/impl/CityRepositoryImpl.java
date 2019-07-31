package com.rolyer.springboothbase.dao.impl;

import com.rolyer.springboothbase.dao.CityRepository;
import com.rolyer.springboothbase.entity.City;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @ClassName: CityRepositoryImpl
 * @Package com.rolyer.springboothbase.dao.impl
 * @Description: ${TOTO} (用一句话描述该文件做什么)
 * @Author rolyer
 * @Date 2019/7/30 14:59
 * @Version V1.0
 */
@Repository
public class CityRepositoryImpl implements CityRepository {
    private static final Logger logger = LoggerFactory.getLogger(CityRepositoryImpl.class);

    @Autowired
    private HbaseTemplate hbaseTemplate;

    public static byte[] family = Bytes.toBytes("info");
    private byte[] name = Bytes.toBytes("name");
    private byte[] code = Bytes.toBytes("code");
    private byte[] parent = Bytes.toBytes("parent");
    private byte[] created = Bytes.toBytes("created");
    private byte[] updated = Bytes.toBytes("updated");

    @Override
    public City save(City entity) {
        return hbaseTemplate.execute("city", (HTableInterface table) -> {

            Put p = new Put(Bytes.toBytes(entity.getCode()));
            City city = new City(entity.getName(), entity.getCode(), entity.getParent());
            p.addColumn(family, CityRepositoryImpl.this.name, Bytes.toBytes(entity.getName()));
            p.addColumn(family, CityRepositoryImpl.this.code, Bytes.toBytes(entity.getCode()));
            p.addColumn(family, CityRepositoryImpl.this.parent, Bytes.toBytes(entity.getParent()));
            if (entity.getCreated()!=null) {
                p.addColumn(family, CityRepositoryImpl.this.created, Bytes.toBytes(entity.getCreated().longValue()));
            }
            p.addColumn(family, CityRepositoryImpl.this.updated, Bytes.toBytes(entity.getUpdated().longValue()));

            table.put(p);

            return city;
        });
    }

    @Override
    public boolean delete(String code) {
        hbaseTemplate.delete("city", code,"info");

        return true;
    }

    @Override
    public City update(City city) {
        return save(city);
    }

    @Override
    public List<City> findAll() {
        return hbaseTemplate.find("city", "info", (Result result, int rowNum) -> {
            logger.debug("rowNum: {}", rowNum);

            City city = new City(Bytes.toString(result.getValue(family, name)),
                    Bytes.toString(result.getValue(family, code)),
                    Bytes.toString(result.getValue(family, parent)));

            byte[] resCreated = result.getValue(family, created);
            city.setCreated(ObjectUtils.isEmpty(resCreated) ? Long.valueOf(0) : Bytes.toLong(resCreated));

            byte[] resUpdated = result.getValue(family, updated);
            city.setUpdated(ObjectUtils.isEmpty(resUpdated) ? Long.valueOf(0) : Bytes.toLong(resUpdated));

            return city;
        });
    }
}
