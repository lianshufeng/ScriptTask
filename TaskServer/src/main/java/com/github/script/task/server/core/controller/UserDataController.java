package com.github.script.task.server.core.controller;

import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.server.core.model.DataSetModel;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("userdata")
public class UserDataController {

    @RequestMapping("push")
    public Object push(String user, String hash, String text) {
        Assert.hasText(user, "用户标识不能为空");
//        return dataSetService.push(text);

        return null;
    }


}
