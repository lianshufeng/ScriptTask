package com.github.script.task.bridge.service;


import com.github.script.task.bridge.model.param.JobParam;
import com.github.script.task.bridge.result.ResultContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class JobService extends SuperService {

    public ResultContent createJobByTaskId(String taskId) {
        try {
            return postForm("job/createByTaskId", Map.of("taskId", taskId), ResultContent.class);
        } catch (Exception e) {
            log.error("exception : {}", e.getMessage());
        }
        return null;
    }

    /**
     * 获取一个Job
     *
     * @param param
     * @return
     */
    public ResultContent getJob(JobParam param) {
        try {
            return postJson("job/get", param, ResultContent.class);
        } catch (Exception e) {
            log.error("exception : {}", e.getMessage());
        }
        return null;
    }

    public ResultContent updateJobCreatTime(String jobId,Long createTime){
        try {
            return postForm("job/updateJobCreatTime", Map.of("jobId", jobId,"createTime",createTime), ResultContent.class);
        } catch (Exception e) {
            log.error("exception : {}", e.getMessage());
        }
        return null;
    }

}
