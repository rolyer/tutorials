package com.scc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by rolyer on 15-1-7.
 */
@Controller
@RequestMapping("/")
public class HomeController {

    /**
     * @param out
     */
    @RequestMapping("index.htm")
    public void index(ModelMap out) {
        out.put("msg", "If you can read this page it means that the application is working properly.");
    }
}
