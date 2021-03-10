package top.dzurl.task.server.core.dao.extend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import top.dzurl.task.bridge.model.param.TaskParam;
import top.dzurl.task.server.core.domain.Task;

public interface TaskDaoExtend {

    Page<Task> list(TaskParam param, Pageable pageable);

    Boolean del(String id);

    Boolean update(TaskParam param);

}
