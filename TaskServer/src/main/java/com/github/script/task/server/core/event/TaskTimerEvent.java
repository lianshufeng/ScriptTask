package com.github.script.task.server.core.event;

import com.github.script.task.server.core.domain.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.script.task.bridge.model.JobModel;
import com.github.script.task.bridge.util.JsonUtil;
import com.github.script.task.server.core.service.JobService;
import com.github.script.task.server.other.timer.event.SimpleTaskTimerEvent;

@Slf4j
@Component
public class TaskTimerEvent implements SimpleTaskTimerEvent<Task> {

    @Autowired
    private JobService jobService;

    @Override
    public void execute(Task task) {
        JobModel jobModel = jobService.createByTask(task);
        log.info("创建job -> {}", JsonUtil.toJson(jobModel));
    }
}
