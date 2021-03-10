package top.dzurl.task.server.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.dzurl.task.bridge.model.param.TaskParam;
import top.dzurl.task.bridge.result.ResultContent;
import top.dzurl.task.server.core.service.TaskService;

@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    public Object create(TaskParam param){
        Assert.hasText(param.getCron(),"cron表达式不能为空");
        Assert.hasText(param.getScriptName(),"脚本名称不能为空");

        return taskService.create(param);
    }
}
