package com.github.script.task.bridge.service;


import com.github.script.task.bridge.model.param.JobParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class JobService extends SuperService {

    public Object createJobByTaskId(String taskId){

        try {
            return postForm("job/createByTaskId", Map.of("taskId",taskId), Object.class);
        } catch (Exception e) {
            log.error("exception : {}", e.getMessage());
        }
        return null;
    }

    public Object delJob(String jobId){
        return null;
    }
}
