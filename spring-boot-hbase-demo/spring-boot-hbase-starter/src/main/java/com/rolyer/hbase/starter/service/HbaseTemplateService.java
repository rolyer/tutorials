package com.rolyer.hbase.starter.service;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: HbaseTemplateService
 * @Package com.rolyer.hbase.starter.service
 * @Description: hbaseTemplate服务接口
 * @Author rolyer
 * @Date 2019/7/31 16:59
 * @Version V1.0
 */
public interface HbaseTemplateService {

    /**
     * 在HBase上面创建表
     *
     * @param tableName   表名
     * @param familyNames 列族名(可以同时传入多个列族名)
     * @return
     * @throws IOException
     */
    boolean createTable(String tableName, String... familyNames) throws IOException;

    /**
     * 查询所有的hbase数据
     *
     * @param tableName 表名
     * @param family    列族名
     * @param clazz     返回数据类型
     * @param <T>       返回数据类型
     * @return
     */
    <T> List<T> searchAll(String tableName, String family, Class<T> clazz);

    /**
     * //TODO: Scan 查询所有的hbase数据
     *
     * @param tableName 表名
     * @param scan      列族名
     * @param clazz     返回数据类型
     * @param <T>       返回数据类型
     * @return
     */
    <T> List<T> searchAll(String tableName, Scan scan, Class<T> clazz);

    /**
     * 向表中插入数据
     *
     * @param object
     * @param tableName
     * @param family
     * @param rowkey
     * @return
     */
    <T> T createOne(T object, String tableName, String family, String rowkey);

    /**
     * 通过表名和rowkey获取一行数据转object
     *
     * @param <T>       数据类型
     * @param tableName 表名
     * @param rowkey    主键
     * @return
     */
    <T> T getOneToClass(Class<T> clazz, String tableName, String family, String rowkey);

    /**
     * 根据表名组合查询
     *
     * @param clazz
     * @param tableName
     * @param filterList 查询条件过滤器列表
     * @param <T>
     * @return
     */
    <T> List<T> getListByCondition(Class<T> clazz, String tableName, FilterList filterList);

    /**
     * 通过表名和rowkey获取一行map数据
     *
     * @param tableName
     * @param rowName
     * @return
     */
    Map<String, Object> getOneToMap(String tableName, String rowName);

    /**
     * 查询一条记录一个column的值
     *
     * @param tableName 表名
     * @param rowkey
     * @param family    列族
     * @param column    列
     * @return
     */
    String getColumn(String tableName, String rowkey, String family, String column);

    /**
     * 查询开始row和结束row之间的数据
     *
     * @param <T>       数据类型
     * @param tableName 表名
     * @param startRow  开始row
     * @param endRow    结束row
     * @return
     */
    <T> List<T> findByRowRange(Class<T> clazz, String tableName, String startRow, String endRow);

    /**
     * *SingleColumnValueFilter scvf = new SingleColumnValueFilter(
     * Bytes.toBytes(family),  //搜索哪个列族
     * Bytes.toBytes(column),   //搜素哪一列
     * CompareFilter.CompareOp.EQUAL, //对比关系
     * Bytes.toBytes(Keywords)); //这里传入 SubstringComparator 比较器,搜索的结果为列值(value)包含关键字,传入bytes数组,则进行完全匹配
     * scvf.setLatestVersionOnly(true); //属性设置为true时,如果查询的列族下,没有colume这个列,则不返回这行数据,反之就返回这行数据
     *
     * @param tableName
     * @param scvf
     * @param clazz
     * @param <T>
     * @return
     */
    <T> List<T> searchAllByFilter(Class<T> clazz, String tableName, SingleColumnValueFilter scvf);
}
