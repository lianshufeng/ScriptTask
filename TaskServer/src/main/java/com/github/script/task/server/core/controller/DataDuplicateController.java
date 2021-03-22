package com.github.script.task.server.core.controller;

import com.github.script.task.bridge.model.param.DataDuplicateAndSaveParam;
import com.github.script.task.server.core.service.DataDuplicateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("data")
public class DataDuplicateController {

    @Autowired
    private DataDuplicateService removeDuplicateService;

    @RequestMapping("duplicateAndSave")
    public Object duplicateAndSave(@RequestBody DataDuplicateAndSaveParam param) {
        Assert.hasText(param.getKey(), "脚本名称不能为空");
        Assert.notNull(param.getValues(), "值不能为空");
        return removeDuplicateService.duplicateAndSave(param);
    }
}
