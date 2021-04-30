package com.github.script.task.server.core.controller;

import com.github.script.task.bridge.model.param.UpdateTaskParam;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.script.task.bridge.model.param.TaskParam;
import com.github.script.task.server.core.service.TaskService;

@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private DBHelper dbHelper;

    /**
     * 创建任务
     * @param param
     * @return
     */
    @RequestMapping("create")
    public Object create(@RequestBody TaskParam param){
        Assert.hasText(param.getScriptName(),"脚本名称不能为空");

        return taskService.create(param);
    }

    /**
     * 修改任务
     * @param param
     * @return
     */
    @RequestMapping("update")
    public Object update(@RequestBody UpdateTaskParam param){
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
     * @param param
     * @return
     */
    @RequestMapping("del")
    public Object del(@RequestBody TaskParam param){
        Assert.hasText(param.getId(),"任务ID不能为空");
        return taskService.del(param.getId());
    }

    /**
     * 获取任务
     * @param id
     * @return
     */
    @RequestMapping("get")
    public Object get(String id){
        return taskService.findById(id);
    }

    /**
     * 通过脚本名称修改参数
     * @param param
     * @return
     */
    @RequestMapping("updateParamByScript")
    public Object updateParamByScript(@RequestBody TaskParam param){
        Assert.hasText(param.getScriptName(),"脚本名称能为空");
        return taskService.updateParamByScript(param);
    }
}
