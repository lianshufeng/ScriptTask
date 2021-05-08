package com.github.script.task.server.core.controller;

import com.github.script.task.server.core.model.UserDataModel;
import com.github.script.task.server.core.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("userdata")
public class UserDataController {

    @Autowired
    private UserDataService userDataService;


    @RequestMapping("push")
    public Object push(UserDataModel model) {
        Assert.hasText(model.getPlatform(), "平台标识不能为空");
        Assert.hasText(model.getUser(), "用户标识不能为空");
        return userDataService.push(model);
    }


    @RequestMapping("push.json")
    public Object pushJson(@RequestBody UserDataModel model) {
        Assert.hasText(model.getPlatform(), "平台标识不能为空");
        Assert.hasText(model.getUser(), "用户标识不能为空");
        return userDataService.push(model);
    }


}
