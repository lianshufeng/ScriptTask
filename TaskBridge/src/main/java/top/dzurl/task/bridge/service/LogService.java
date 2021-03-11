package top.dzurl.task.bridge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import top.dzurl.task.bridge.model.param.JobParam;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LogService extends SuperService {

    private Map<String, List<String>> jobInfos = new ConcurrentHashMap<>();

    private Timer timer = new Timer();


    @Autowired
    private void postTimer(ApplicationContext applicationContext) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                triggerPostLogs();
            }
        }, 0, 3000l);
    }


    @Autowired
    private void shutdown(ApplicationContext applicationContext) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            timer.cancel();
            triggerPostLogs();
        }));
    }

    /**
     * 添加日志
     *
     * @param jobId
     * @param info
     */
    public void info(String jobId, String info) {
        synchronized (jobId) {
            List<String> infos = this.jobInfos.get(jobId);
            if (infos == null) {
                infos = new ArrayList<>();
                this.jobInfos.put(jobId, infos);
            }
            infos.add(info);
        }
    }


    /**
     * 触发提交日志
     */
    private synchronized void triggerPostLogs() {
        Map<String, List<String>> items = new HashMap<>(this.jobInfos);
        this.jobInfos.clear();
        items.entrySet().parallelStream().forEach((it) -> {
            JobParam jobParam = new JobParam();
            jobParam.setJobId(it.getKey());
            jobParam.setLogs(it.getValue());
            postJson("job/writeLog", jobParam, Object.class);
        });
    }


}
