package com.rolyer.hbase.starter.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rolyer.hbase.starter.service.HbaseTemplateService;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName: HbaseTemplateServiceImpl
 * @Package com.rolyer.hbase.starter.service.impl
 * @Description: ${TOTO} (用一句话描述该文件做什么)
 * @Author rolyer
 * @Date 2019/7/31 17:03
 * @Version V1.0
 */
@Service
public class HbaseTemplateServiceImpl implements HbaseTemplateService {
    private static final Logger logger = LoggerFactory.getLogger(HbaseTemplateServiceImpl.class);

    @Autowired
    private HbaseTemplate hbaseTemplate;
    @Autowired
    private Admin admin;

    /**
     * 创建表
     *
     * @param tableName   表名
     * @param familyNames 列族名
     */
    @Override
    public boolean createTable(String tableName, String... familyNames) throws IOException {
        // 先删除现有表
        deleteTable(tableName);

        TableName table = TableName.valueOf(tableName);
        HTableDescriptor desc = new HTableDescriptor(table);

        for (int i = 0; i < familyNames.length; i++) {
            desc.addFamily(new HColumnDescriptor(familyNames[i]));
        }

        admin.createTable(desc);

        logger.info("This table {} has been successfully created!", tableName);

        return true;
    }

    private boolean deleteTable(String tableName) throws IOException {
        TableName table = TableName.valueOf(tableName);

        //如果存在则删除
        if (admin.tableExists(table)) {
            logger.warn("This table {} already exists!", tableName);

            if (!admin.isTableDisabled(table)) {
                logger.warn("Disabling {}...", tableName);
                admin.disableTable(table);

            }
            logger.warn("Deleting {}...", tableName);
            admin.deleteTable(table);
        }

        return true;
    }

    @Override
    public <T> List<T> searchAll(String tableName, String family, Class<T> clazz) {
        return hbaseTemplate.find(tableName, family, (Result result, int rowNum) -> toObj(clazz, family, result));
    }

    @Override
    public <T> List<T> searchAll(String tableName, Scan scan, Class<T> clazz) {
        //TODO: 实现
        return hbaseTemplate.find(tableName, scan, (Result result, int rowNum) -> null);
    }

    @Override
    public <T> T createOne(T pojo, String tableName, String family, String rowkey) {
        if (pojo == null || StringUtils.isBlank(tableName) || StringUtils.isBlank(family)) {
            return null;
        }

        return hbaseTemplate.execute(tableName, (HTableInterface table) -> {
            PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(pojo.getClass());
            BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(pojo);

            Put p = new Put(Bytes.toBytes(rowkey));

            for (PropertyDescriptor propertyDescriptor : pds) {
                String name = propertyDescriptor.getName();
                Class<?> cls = propertyDescriptor.getPropertyType();

                // 值为null
                Object vobj = beanWrapper.getPropertyValue(name);
                if (ObjectUtils.isEmpty(vobj)) {
                    continue;
                }

                // 值为空
                String v = beanWrapper.getPropertyValue(name).toString();
                if (StringUtils.isBlank(v)) {
                    continue;
                }

                if (cls.getName().equals(String.class.getName())) {
                    p.addColumn(Bytes.toBytes(family), Bytes.toBytes(name), Bytes.toBytes(v));
                }
                else if (cls.getName().equals(Long.class.getName())) {
                    p.addColumn(Bytes.toBytes(family), Bytes.toBytes(name), Bytes.toBytes(Long.valueOf(v).longValue()));
                }
            }

            table.put(p);

            return pojo;
        });
    }

    @Override
    public <T> T getOneToClass(Class<T> clazz, String tableName, String family, String rowkey) {
        if (clazz == null || StringUtils.isBlank(tableName) || StringUtils.isBlank(rowkey)) {
            return null;
        }

        return hbaseTemplate.get(tableName, rowkey, (Result result, int rowNum) -> toObj(clazz, family, result));
    }

    private <T> T toObj(Class<T> c, String family, Result result) throws Exception {
        byte[] f = Bytes.toBytes(family);

        T pojo = c.newInstance();

        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(pojo.getClass());
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(pojo);

        for (PropertyDescriptor propertyDescriptor : pds) {
            String name = propertyDescriptor.getName();
            Class<?> cls = propertyDescriptor.getPropertyType();
            byte[] qualifier = Bytes.toBytes(name);
            byte[] v = result.getValue(f, qualifier);

            if (!ObjectUtils.isEmpty(v)) {
                if (cls.getName().equals(String.class.getName())) {
                    beanWrapper.setPropertyValue(name, Bytes.toString(v));
                }
                if (cls.getName().equals(Long.class.getName())) {
                    beanWrapper.setPropertyValue(name, Bytes.toLong(v));
                }
            }
        }

        return pojo;
    }

    @Override
    public <T> List<T> getListByCondition(Class<T> c, String tableName, FilterList filterList) {
        if (c == null || StringUtils.isBlank(tableName)) {
            return null;
        }
//        List<Filter>  list=new ArrayList<>();
//        String targetSet=jsonObject.getString("targetSet");
//        String targetSonSet=jsonObject.getString("targetSonSet");
//        String target=jsonObject.getString("target");
//        if(StringUtils.isNotBlank(targetSet)){
//            list.add(new SingleColumnValueFilter(Bytes.toBytes("targetSet"),null,
//                    CompareFilter.CompareOp.EQUAL,Bytes.toBytes(targetSet)));
//        }
//        if(StringUtils.isNotBlank(targetSonSet)){
//            list.add(new SingleColumnValueFilter(Bytes.toBytes("targetSonSet"),null,
//                    CompareFilter.CompareOp.EQUAL,Bytes.toBytes(targetSonSet)));
//        }
//        if(StringUtils.isNotBlank(target)){
//            list.add(new SingleColumnValueFilter(Bytes.toBytes("target"),null,
//                    CompareFilter.CompareOp.EQUAL,Bytes.toBytes(target)));
//        }
//        FilterList filterList=new FilterList(list);
        Scan scan = new Scan();
        scan.setFilter(filterList);
        return hbaseTemplate.find(tableName, scan, (Result result, int rowNum)->toObj(c, result));
    }

    @Override
    public Map<String, Object> getOneToMap(String tableName, String rowName) {
        return hbaseTemplate.get(tableName, rowName, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(Result result, int i) throws Exception {
                List<Cell> ceList = result.listCells();
                Map<String, Object> map = new HashMap<String, Object>();
                if (ceList != null && ceList.size() > 0) {
                    for (Cell cell : ceList) {
                        map.put(Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength()) +
                                        "_" + Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()),
                                Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                    }
                }
                return map;
            }
        });
    }

    @Override
    public String getColumn(String tableName, String rowkey, String family, String column) {
        if (StringUtils.isBlank(tableName) || StringUtils.isBlank(family)
                || StringUtils.isBlank(rowkey) || StringUtils.isBlank(column)) {
            return null;
        }
        return hbaseTemplate.get(tableName, rowkey, family, column, new RowMapper<String>() {
            @Override
            public String mapRow(Result result, int rowNum) throws Exception {
                List<Cell> ceList = result.listCells();
                String res = "";
                if (ceList != null && ceList.size() > 0) {
                    for (Cell cell : ceList) {
                        res =
                                Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                    }
                }
                return res;
            }
        });
    }

    @Override
    public <T> List<T> findByRowRange(Class<T> c, String tableName, String startRow, String endRow) {
        if (c == null || StringUtils.isBlank(tableName) || StringUtils.isBlank(startRow)
                || StringUtils.isBlank(endRow)) {
            return null;
        }
        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes(startRow));
        scan.setStopRow(Bytes.toBytes(endRow));
        scan.setCacheBlocks(false);
        scan.setCaching(2000);
        return hbaseTemplate.find(tableName, scan, (Result result, int rowNum)->toObj(c, result));
    }

    private <T>T toObj(Class<T> clazz, Result result) throws Exception {
        T pojo = clazz.newInstance();
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(pojo);
        List<Cell> ceList = result.listCells();
        for (Cell cellItem : ceList) {
            String cellName = new String(CellUtil.cloneQualifier(cellItem));
            if (!"class".equals(cellName)) {
                beanWrapper.setPropertyValue(cellName, new String(CellUtil.cloneValue(cellItem)));
            }
        }

        return pojo;
    }

    @Override
    public <T> List<T> searchAllByFilter(Class<T> clazz, String tableName, SingleColumnValueFilter scvf) {
        Scan scan = new Scan();
        scan.setFilter(scvf);
        return hbaseTemplate.find(tableName, scan, new RowMapper<T>() {
            @Override
            public T mapRow(Result result, int rowNum) throws Exception {
                List<Cell> ceList = result.listCells();
                JSONObject obj = new JSONObject();
                T item = clazz.newInstance();
                if (ceList != null && ceList.size() > 0) {
                    for (Cell cell : ceList) {
                        obj.put(
                                Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(),
                                        cell.getQualifierLength()),
                                Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                    }
                } else {
                    return null;
                }
                item = JSON.parseObject(obj.toJSONString(), clazz);
                return item;
            }
        });
    }
}
