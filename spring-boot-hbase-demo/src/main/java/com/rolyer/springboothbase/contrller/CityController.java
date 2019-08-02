package com.rolyer.springboothbase.contrller;

import com.rolyer.springboothbase.entity.City;
import com.rolyer.springboothbase.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<City> save(@RequestBody City city) {
        city.setCreated(System.currentTimeMillis());
        city.setUpdated(System.currentTimeMillis());
        City c = cityService.createOne(city, "city", "info", "code");

        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<City> update(@RequestBody City city) {
        city.setUpdated(System.currentTimeMillis());
        City c = cityService.createOne(city, "city", "info", "code");

        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Boolean> delete(String code) {
        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<City>> list() {
        List<City> cities = cityService.searchAll("city", "info", City.class);

        return new ResponseEntity<>(cities, HttpStatus.OK);
    }
}
