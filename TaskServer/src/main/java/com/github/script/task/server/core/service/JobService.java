package com.github.script.task.server.core.service;

import com.github.script.task.server.core.dao.JobDao;
import com.github.script.task.server.core.dao.JobRunLogDao;
import com.github.script.task.server.core.dao.TaskDao;
import com.github.script.task.server.core.domain.Job;
import com.github.script.task.server.core.domain.JobRunLog;
import com.github.script.task.server.core.domain.Task;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.script.task.bridge.model.JobModel;
import com.github.script.task.bridge.model.param.JobLogParam;
import com.github.script.task.bridge.model.param.JobParam;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;

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

    @Autowired
    private TaskDao taskDao;


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

    public ResultContent<String> createByTaskId(String taskId) {
        return null;
    }
}
