package com.rolyer.springboothbase.contrller;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @ClassName: DemoContrller
 * @Package com.rolyer.springboothbase.contrller
 * @Description: ${TOTO} (用一句话描述该文件做什么)
 * @Author rolyer
 * @Date 2019/7/26 17:35
 * @Version V1.0
 */
@RestController
@RequestMapping("/tables")
public class DemoContrller {

    private static final Logger logger = LoggerFactory.getLogger(DemoContrller.class);

    @Autowired
    private Admin admin;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<Boolean> create(String tableName, String familyName) throws IOException {
        Assert.hasText(tableName, "tableName must not be null, empty, or blank");

        boolean res = createTable(tableName, StringUtils.isNotBlank(familyName) ? familyName : "info");

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Boolean> delete(String tableName) throws IOException {
        Assert.hasText(tableName, "tableName must not be null, empty, or blank");

        boolean res = deleteTable(tableName);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * 创建表
     *
     * @param tableName  表名
     * @param familyName 列族名
     */
    private boolean createTable(String tableName, String familyName) throws IOException {
        // 先删除现有表
        deleteTable(tableName);

        TableName table = TableName.valueOf(tableName);
        HTableDescriptor desc = new HTableDescriptor(table);

        final byte[] fn = Bytes.toBytes(familyName);
        HColumnDescriptor family = new HColumnDescriptor(fn);

        desc.addFamily(family);

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
}
