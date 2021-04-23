package com.github.script.task.server.core.scheduled;

import com.github.script.task.server.core.dao.JobDao;
import com.github.script.task.server.core.dao.TaskDao;
import com.github.script.task.server.core.domain.Job;
import com.github.script.task.server.other.token.service.ResourceTokenService;
import lombok.Cleanup;
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

    @Autowired
    private ResourceTokenService resourceTokenService;


    @Scheduled(cron = "0 */10 * * * ?")
    private void resetDeice() {
        @Cleanup ResourceTokenService.Token token = resourceTokenService.token("JobTimer_resetDeice");
        log.debug("resetDeice");
        try {
            Job job = jobDao.resetDeice();
            if (job != null) {
                taskDao.resetDeice(job.getTask().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
