package com.github.script.task.server.core.event;

import com.github.script.task.bridge.model.param.RemoveDuplicateParam;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import com.github.script.task.server.core.conf.RemoveDuplicateConf;
import com.github.script.task.server.core.domain.Task;
import com.github.script.task.server.core.service.RemoveDuplicateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.script.task.bridge.model.JobModel;
import com.github.script.task.bridge.util.JsonUtil;
import com.github.script.task.server.core.service.JobService;
import com.github.script.task.server.other.timer.event.SimpleTaskTimerEvent;

import java.util.List;

@Slf4j
@Component
public class TaskTimerEvent implements SimpleTaskTimerEvent<Task> {

    @Autowired
    private JobService jobService;

    @Autowired
    private RemoveDuplicateService removeDuplicateService;

    @Autowired
    private RemoveDuplicateConf removeDuplicateConf;

    @Override
    public void execute(Task task) {
        RemoveDuplicateParam param = new RemoveDuplicateParam();
        param.setValues(List.of(task.getId()));
        param.setScriptName(task.getScriptName());
        param.setTtl(removeDuplicateConf.getDefaultTTl());
        ResultContent<List<String>> content = removeDuplicateService.duplicateAndSave(param);
        if (content.getState() == ResultState.Success){
            JobModel jobModel = jobService.createByTask(task);
            log.info("创建job -> {}", JsonUtil.toJson(jobModel));
        }
    }
}
