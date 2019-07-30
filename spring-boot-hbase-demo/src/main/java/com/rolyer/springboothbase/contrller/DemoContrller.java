package com.rolyer.springboothbase.contrller;

import com.rolyer.springboothbase.entity.City;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName: DemoContrller
 * @Package com.rolyer.springboothbase.contrller
 * @Description: ${TOTO} (用一句话描述该文件做什么)
 * @Author rolyer
 * @Date 2019/7/26 17:35
 * @Version V1.0
 */
@RestController
@RequestMapping("/hbase")
public class DemoContrller {

    private static final Logger logger = LoggerFactory.getLogger(DemoContrller.class);

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Autowired
    private Admin admin;

    public static byte[] family = Bytes.toBytes("info");
    private byte[] name = Bytes.toBytes("name");
    private byte[] code = Bytes.toBytes("code");
    private byte[] parent = Bytes.toBytes("parent");

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> create() throws IOException {
        createTable("city", "info");

        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> delete() throws IOException {
        deleteTable("city");

        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<City> save(@RequestBody City city) throws IOException {
        City c = save(city.getName(), city.getCode(), city.getParent());

        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @GetMapping("/query")
    @ResponseBody
    public ResponseEntity<List<City>> query() throws IOException {
        List<City> cities = list();

        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    public List<City> list() {
        return hbaseTemplate.find("city", "info", new RowMapper<City>() {
            @Override
            public City mapRow(Result result, int rowNum) throws Exception {
                logger.info("rowNum: {}", rowNum);
                return new City(Bytes.toString(result.getValue(family, name)),
                        Bytes.toString(result.getValue(family, code)),
                        Bytes.toString(result.getValue(family, parent)));
            }
        });
    }

    public City save(final String name, final String code, final String parent) {

        return hbaseTemplate.execute("city", new TableCallback<City>() {
            @Override
            public City doInTable(HTableInterface table) throws Throwable {
                City city = new City(name, code, parent);
                Put p = new Put(Bytes.toBytes(city.getCode()));
                p.addColumn(family, DemoContrller.this.name, Bytes.toBytes(city.getName()));
                p.addColumn(family, DemoContrller.this.code, Bytes.toBytes(city.getCode()));
                p.addColumn(family, DemoContrller.this.parent, Bytes.toBytes(city.getParent()));
                table.put(p);

                return city;

            }
        });
    }

    public List<Result> getRowKeyAndColumn(String tableName, String startRowkey, String stopRowkey, String column, String qualifier) {
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        if (StringUtils.isNotBlank(column)) {
            logger.debug("{}", column);
            filterList.addFilter(new FamilyFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(column))));
        }
        if (StringUtils.isNotBlank(qualifier)) {
            logger.debug("{}", qualifier);
            filterList.addFilter(new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(qualifier))));
        }
        Scan scan = new Scan();
        if (filterList.getFilters().size() > 0) {
            scan.setFilter(filterList);
        }
        scan.withStartRow(Bytes.toBytes(startRowkey));
        scan.withStopRow(Bytes.toBytes(stopRowkey));

        return hbaseTemplate.find(tableName, scan, (rowMapper, rowNum) -> rowMapper);
    }

    /**
     * 创建表
     *
     * @param tableName  表名
     * @param familyName 列族名
     */
    public void createTable(String tableName, String familyName) throws IOException {
        // 先删除现有表
        deleteTable(tableName);

        TableName table = TableName.valueOf(tableName);
        HTableDescriptor desc = new HTableDescriptor(table);

        final byte[] fn = Bytes.toBytes(familyName);
        HColumnDescriptor family = new HColumnDescriptor(fn);

        desc.addFamily(family);

        admin.createTable(desc);

        logger.info("This table {} has been successfully created!", tableName);
    }

    public void deleteTable(String tableName) throws IOException {
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
    }
}
