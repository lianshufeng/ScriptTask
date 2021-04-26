package com.github.script.task.bridge.script.action.task;

import com.github.script.task.bridge.conf.WebFetchConf;
import com.github.script.task.bridge.model.TaskModel;
import com.github.script.task.bridge.model.param.TaskParam;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import com.github.script.task.bridge.script.SuperScriptAction;
import com.github.script.task.bridge.service.JobService;
import com.github.script.task.bridge.service.TaskService;
import com.github.script.task.bridge.util.HttpClient;
import com.github.script.task.bridge.util.HttpClientUtil;
import com.github.script.task.bridge.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
public class TaskAction extends SuperScriptAction {


    @Autowired
    private TaskService taskService;

    @Autowired
    private JobService jobService;

    public void createTask(TaskParam param,boolean isCreateJob){
        ResultContent resultContent = taskService.creatTask(param);
        if (resultContent.getState() == ResultState.Success && isCreateJob){
            Map task = (Map) resultContent.getContent();
            jobService.createJobByTaskId((String) task.get("id"));
        }
    }

    public void  delTask(String id){
        taskService.del(id);
    }
}
