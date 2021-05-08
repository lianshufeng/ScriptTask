package com.github.script.task.server.core.dao.mongo.extend;

import com.github.script.task.bridge.model.param.UpdateTaskParam;
import com.github.script.task.server.core.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.github.script.task.bridge.model.param.TaskParam;

public interface TaskDaoExtend {

    Page<Task> list(TaskParam param, Pageable pageable);

    Boolean del(String id);

    Boolean update(UpdateTaskParam param);

    Task resetDeice(String id);

    Boolean updateParamByScript(TaskParam param);
}
