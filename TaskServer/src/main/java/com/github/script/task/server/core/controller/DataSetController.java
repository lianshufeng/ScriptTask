package com.github.script.task.server.core.controller;

import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.server.core.model.DataSetModel;
import com.github.script.task.server.core.service.DataSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据集控制层
 */
@RestController
@RequestMapping("dataset")
public class DataSetController {

    @Autowired
    private DataSetService dataSetService;


    @RequestMapping("push")
    public ResultContent<DataSetModel> push(String text) {
        return dataSetService.push(text);
    }


    @RequestMapping("hash")
    public ResultContent<DataSetModel> hash(String hash) {
        return dataSetService.hash(hash);
    }


}
