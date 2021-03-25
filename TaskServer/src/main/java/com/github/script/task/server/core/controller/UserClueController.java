package com.github.script.task.server.core.controller;

import com.github.script.task.bridge.model.param.UserClueParam;
import com.github.script.task.server.core.service.UserClueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("userClue")
public class UserClueController {

    @Autowired
    private UserClueService userClueService;


    @RequestMapping("save")
    public Object save(@RequestBody UserClueParam param){
        return userClueService.save(param);
    }


    @RequestMapping("list")
    public Object list(UserClueParam param, Pageable pageable){
        return userClueService.list(param,pageable);
    }

}
