package com.github.script.task.server.core.controller;

import com.github.script.task.bridge.model.param.TaskParam;
import com.github.script.task.server.core.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.script.task.bridge.model.param.JobLogParam;
import com.github.script.task.bridge.model.param.JobParam;

@RestController
@RequestMapping("job")
public class JobController {

    @Autowired
    private JobService jobService;

    @RequestMapping("get")
    public Object get(@RequestBody JobParam param){
        Assert.notNull(param.getCount(),"获取条数不能为空");
        Assert.isTrue(param.getCount()  > 0,"获取条数不能小于0");
        return jobService.get(param);
    }

    @RequestMapping("writeLog")
    public Object writeLog(@RequestBody JobLogParam param){
        return jobService.writeLog(param);
    }


    /**
     * 创建job
     * @param taskId
     * @return
     */
    @RequestMapping("createByTaskId")
    public Object createByTaskId(String taskId){
        Assert.hasText(taskId,"任务id不能为空");
        return jobService.createByTaskId(taskId);
    }
}
