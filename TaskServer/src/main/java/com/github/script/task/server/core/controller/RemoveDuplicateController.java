package com.github.script.task.server.core.controller;

import com.github.script.task.bridge.model.param.RemoveDuplicateParam;
import com.github.script.task.server.core.service.RemoveDuplicateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("data")
public class RemoveDuplicateController {

    @Autowired
    private RemoveDuplicateService removeDuplicateService;

    @RequestMapping("duplicateAndSave")
    public Object duplicateAndSave(@RequestBody RemoveDuplicateParam param){
        Assert.hasText(param.getScriptName(),"脚本名称不能为空");
        Assert.notNull(param.getValues(),"值不能为空");
        return removeDuplicateService.duplicateAndSave(param);
    }
}
