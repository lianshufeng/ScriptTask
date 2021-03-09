package top.dzurl.task.server.core.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.dzurl.task.server.core.domain.Task;
import top.dzurl.task.server.other.timer.event.SimpleTaskTimerEvent;

@Slf4j
@Component
public class TaskTimerEvent implements SimpleTaskTimerEvent<Task> {

    @Override
    public void execute(Task task) {
        log.info("time : " + task.getId());
    }
}
