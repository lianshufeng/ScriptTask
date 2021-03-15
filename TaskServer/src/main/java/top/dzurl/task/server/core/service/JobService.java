package top.dzurl.task.server.core.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.dzurl.task.bridge.model.JobModel;
import top.dzurl.task.bridge.model.param.JobLogParam;
import top.dzurl.task.bridge.model.param.JobParam;
import top.dzurl.task.bridge.result.ResultContent;
import top.dzurl.task.bridge.result.ResultState;
import top.dzurl.task.server.core.dao.JobDao;
import top.dzurl.task.server.core.dao.JobRunLogDao;
import top.dzurl.task.server.core.domain.Job;
import top.dzurl.task.server.core.domain.JobRunLog;
import top.dzurl.task.server.core.domain.Task;
import top.dzurl.task.server.other.mongo.helper.DBHelper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobDao jobDao;

    @Autowired
    private JobRunLogDao jobRunLogDao;

    @Autowired
    private DBHelper dbHelper;


    public JobModel createByTask(Task task){
        Job job = new Job();
        BeanUtils.copyProperties(task,job,"id");
        job.setTask(task);
        jobDao.save(job);
        return toModel(job);
    }

    /**
     * 获取job
     *
     * @param param
     * @return
     */
    public List<JobModel> get(JobParam param) {
        List<Job> jobs = jobDao.get(param);
        return jobs.stream().map((it) -> {
            createLog(it);
            return toModel(it);
        }).collect(Collectors.toList());
    }

    /**
     * 创建日志
     *
     * @param job
     */
    private void createLog(Job job) {
        if (job != null) {
            JobRunLog log = new JobRunLog();
            log.setScriptName(job.getScriptName());
            log.setScriptName(job.getDeviceId());
            log.setGetJobTime(dbHelper.getTime());
            log.setJobId(job.getId());
            jobRunLogDao.save(log);
        }
    }

    /**
     * 写工作日志
     *
     * @param param
     * @return
     */
    public ResultContent<String> writeLog(JobLogParam param) {
        return this.jobRunLogDao.appendLogs(param.getJobId(), param.getLogs()) ? ResultContent.build(ResultState.Success) : ResultContent.build(ResultState.Success);
    }

    /**
     * 转换到模型
     *
     * @param job
     * @return
     */
    public JobModel toModel(Job job) {
        if (job == null) {
            return null;
        }
        JobModel model = new JobModel();
        BeanUtils.copyProperties(job, model);
        return model;
    }
}
