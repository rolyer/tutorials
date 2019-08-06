package com.rolyer.springboothbase.contrller;

import com.rolyer.hbase.starter.service.HbaseTemplateService;
import com.rolyer.springboothbase.entity.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName: CityController
 * @Package com.rolyer.springboothbase.contrller
 * @Description: ${TOTO} (用一句话描述该文件做什么)
 * @Author rolyer
 * @Date 2019/7/30 15:27
 * @Version V1.0
 */
@RestController
@RequestMapping("/provinces")
public class ProvinceController {

    private final static  String tableName = "province";
    private final static  String family = "info";
    private final static  String rowkey = "code";

    @Autowired
    private HbaseTemplateService hbaseTemplateService;

    @GetMapping("/init")
    @ResponseBody
    public ResponseEntity<Boolean> save() throws IOException {
        return new ResponseEntity<>(hbaseTemplateService.createTable(tableName, family), HttpStatus.OK);
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<Province> save(@RequestBody Province province) {
        province.setCreated(System.currentTimeMillis());
        province.setUpdated(System.currentTimeMillis());
        Province obj = hbaseTemplateService.createOne(province, tableName, family, rowkey);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Province> update(@RequestBody Province province) {
        province.setUpdated(System.currentTimeMillis());
        Province obj = hbaseTemplateService.createOne(province, tableName, family, rowkey);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Boolean> delete() {


        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Province>> list() {
        List<Province> provinces = hbaseTemplateService.searchAll(tableName, family, Province.class);

        return new ResponseEntity<>(provinces, HttpStatus.OK);
    }
}
