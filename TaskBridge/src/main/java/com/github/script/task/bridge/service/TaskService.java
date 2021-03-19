package com.github.script.task.bridge.service;

import com.github.script.task.bridge.model.param.TaskParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskService extends SuperService {

    public Object creatTask(TaskParam param){
        try {
            return postJson("task/create", param, Object.class);
        } catch (Exception e) {
            log.error("exception : {}", e.getMessage());
        }
        return null;
    }

}
