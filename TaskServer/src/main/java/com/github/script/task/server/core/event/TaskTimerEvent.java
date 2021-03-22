package com.github.script.task.server.core.event;

import com.github.script.task.bridge.model.param.DataDuplicateAndSaveParam;
import com.github.script.task.bridge.model.param.DataDuplicateParam;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import com.github.script.task.server.core.conf.TTLConf;
import com.github.script.task.server.core.domain.Task;
import com.github.script.task.server.core.service.DataDuplicateService;
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
    private DataDuplicateService dataDuplicateService;

    @Autowired
    private TTLConf ttlConf;

    //默认的超时锁
    private final static long LockTimeOut = 60000;


    @Override
    public void execute(Task task) {
        DataDuplicateAndSaveParam param = new DataDuplicateAndSaveParam();
        param.setValues(List.of(task.getId()));
        param.setKey(task.getScriptName());
        param.setTtl(LockTimeOut);
        ResultContent<List<String>> content = dataDuplicateService.duplicateAndSave(param);
        if (content.getState() == ResultState.Success) {
            JobModel jobModel = jobService.createByTask(task);
            log.info("创建job -> {}", JsonUtil.toJson(jobModel));

            //删除锁
            dataDuplicateService.remove(DataDuplicateParam
                    .builder()
                    .key(task.getScriptName())
                    .values(content.getContent())
                    .build());

        }
    }


}
