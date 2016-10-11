package com.self.shiro.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Descriptions : index.html
 * Create : 2016/6/20 10:53
 * Update : 2016/6/20 10:53
 */
@Controller
@RequestMapping("/index")
public class IndexController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);
    @RequestMapping(value = "")
    @ResponseBody
    public ModelAndView index() {
        return new ModelAndView("page/index/nav");
    }
}
