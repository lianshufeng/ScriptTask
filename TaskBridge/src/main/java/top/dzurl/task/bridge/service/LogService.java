package top.dzurl.task.bridge.service;

import org.springframework.stereotype.Service;
import top.dzurl.task.bridge.model.param.JobParam;

import java.util.List;

@Service
public class LogService extends SuperService {

    /**
     * 添加日志
     *
     * @param jobId
     * @param info
     */
    public void info(String jobId, String info) {
        JobParam jobParam = new JobParam();
        jobParam.setJobId(jobId);
        jobParam.setLogs(List.of(info));
        postJson("job/writeLog", jobParam, Object.class);
    }


}
