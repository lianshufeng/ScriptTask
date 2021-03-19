package com.github.script.task.bridge.service;

import com.github.script.task.bridge.model.param.TaskParam;
import com.github.script.task.bridge.model.param.UpdateTaskParam;
import com.github.script.task.bridge.result.ResultContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class TaskService extends SuperService {

    public Object creatTask(TaskParam param) {
        try {
            return postJson("task/create", param, Object.class);
        } catch (Exception e) {
            log.error("exception : {}", e.getMessage());
        }
        return null;
    }


    /**
     * 绑定任务
     *
     * @param taskId
     * @param deviceId
     * @return
     */
    public ResultContent bindTask(String taskId, String deviceId) {
        try {
            return postJson("task/update", UpdateTaskParam
                    .builder()
                    .id(taskId)
                    .deviceId(deviceId)
                    .build(), ResultContent.class);
        } catch (Exception e) {
            log.error("exception : {}", e.getMessage());
        }
        return null;
    }

}
