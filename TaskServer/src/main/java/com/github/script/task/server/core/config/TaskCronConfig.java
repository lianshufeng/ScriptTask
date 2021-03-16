package com.github.script.task.server.core.config;

import com.github.script.task.server.core.domain.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import com.github.script.task.server.core.event.TaskTimerEvent;
import com.github.script.task.server.other.timer.config.TaskTimerConfiguration;

@Configuration
public class TaskCronConfig extends TaskTimerConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public TaskTimerItem[] register() {
        return new TaskTimerItem[]{
                TaskTimerItem.builder().taskTimerTable(Task.class).taskTimerEvent(applicationContext.getBean(TaskTimerEvent.class)).build()
        };
    }
}
