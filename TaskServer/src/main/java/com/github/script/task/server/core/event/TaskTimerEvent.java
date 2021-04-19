package com.github.script.task.server.core.event;

import com.github.script.task.bridge.model.JobModel;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import com.github.script.task.server.core.domain.Task;
import com.github.script.task.server.core.service.JobService;
import com.github.script.task.server.other.timer.event.SimpleTaskTimerEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskTimerEvent implements SimpleTaskTimerEvent<Task> {

    @Autowired
    private JobService jobService;


    @Override
    public void execute(Task task) {
        ResultContent<JobModel> jobModel = jobService.createByTask(task);
        if (jobModel.getState() == ResultState.Success) {
            log.info("创建job,成功 -> {}", jobModel.getContent().getId());
        } else {
            log.info("创建job,失败 -> {}", jobModel.getState());
        }


    }


}
