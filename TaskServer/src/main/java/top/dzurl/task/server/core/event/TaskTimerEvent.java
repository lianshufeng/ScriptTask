package top.dzurl.task.server.core.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.dzurl.task.server.core.dao.JobDao;
import top.dzurl.task.server.core.domain.Job;
import top.dzurl.task.server.core.domain.Task;
import top.dzurl.task.server.other.timer.event.SimpleTaskTimerEvent;

@Slf4j
@Component
public class TaskTimerEvent implements SimpleTaskTimerEvent<Task> {

    @Autowired
    private JobDao jobDao;

    @Override
    public void execute(Task task) {
        Job job = new Job();
        BeanUtils.copyProperties(task,job,"id");
        job.setTask(task);
        jobDao.save(job);
        log.info("创建job -> {}", job.getId());
    }
}
