package com.github.script.task.server.core.scheduled;

import com.github.script.task.server.core.dao.JobDao;
import com.github.script.task.server.core.dao.TaskDao;
import com.github.script.task.server.core.domain.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobTimer {

    @Autowired
    private JobDao jobDao;

    @Autowired
    private TaskDao taskDao;


    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨兩點執行
    private void resetDeice(){
        Job job = jobDao.resetDeice();
        if (job != null){
            taskDao.resetDeice(job.getTask().getId());
        }
    }

}
