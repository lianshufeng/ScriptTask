package top.dzurl.task.server.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * 创建任务
     * @param param
     * @return
     */
    @RequestMapping("create")
    public Object create(@RequestBody TaskParam param){
        Assert.hasText(param.getCron(),"cron表达式不能为空");
        Assert.hasText(param.getScriptName(),"脚本名称不能为空");
        return taskService.create(param);
    }

    /**
     * 修改任务
     * @param param
     * @return
     */
    @RequestMapping("update")
    public Object update(@RequestBody TaskParam param){
        Assert.hasText(param.getId(),"任务ID不能为空");
        return taskService.update(param);
    }

    /**
     * 任务列表
     * @param param
     * @param pageable
     * @return
     */
    @RequestMapping("list")
    public Object list(TaskParam param, Pageable pageable){
        return taskService.list(param,pageable);
    }

    /**
     * 删除任务
     * @param id
     * @return
     */
    @RequestMapping("del")
    public Object del(String id){
        Assert.hasText(id,"任务ID不能为空");
        return taskService.del(id);
    }
}
