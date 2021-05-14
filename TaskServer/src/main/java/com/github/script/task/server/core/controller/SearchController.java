package com.github.script.task.server.core.controller;

import com.github.script.task.server.core.service.UserDataIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("search")
public class SearchController {
    @Autowired
    private UserDataIndexService userDataIndexService;

    /**
     * 重新创建索引
     */
    @RequestMapping("reIndex")
    public Object reIndex() {
        this.userDataIndexService.reindex();
        return new HashMap<>() {{
            put("time", System.currentTimeMillis());
        }};
    }




}
