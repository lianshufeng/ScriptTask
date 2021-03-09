package top.dzurl.task.server.core.event;

import org.springframework.stereotype.Component;
import top.dzurl.task.server.core.domain.Task;
import top.dzurl.task.server.other.timer.event.SimpleTaskTimerEvent;

@Component
public class TaskTimerEvent implements SimpleTaskTimerEvent<Task> {

    @Override
    public void execute(Task task) {

        System.out.println("time : " + task.getId());


    }
}
