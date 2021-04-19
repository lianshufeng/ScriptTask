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
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import com.github.script.task.bridge.model.JobModel;
import com.github.script.task.bridge.model.param.JobLogParam;
import com.github.script.task.bridge.model.param.JobParam;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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


    /**
     * 创建任务
     *
     * @param task
     * @return
     */
    @Transactional
    public ResultContent<JobModel> createByTask(Task task) {
        if (this.jobDao.existsByTask(task)) {
            return ResultContent.build(ResultState.JobExists);
        }
        Job job = new Job();
        BeanUtils.copyProperties(task, job, "id");
        job.setTask(task);
        this.dbHelper.saveTime(job);
        jobDao.save(job);
        return ResultContent.buildContent(toModel(job));
    }


    /**
     * 创建任务
     *
     * @param taskId
     * @return
     */
    @Transactional
    public ResultContent<JobModel> createByTaskId(String taskId) {
        Optional<Task> optional = taskDao.findById(taskId);
        if (!optional.isPresent()) {
            return ResultContent.build(ResultState.TaskNoneExists);
        }
        return this.createByTask(optional.get());
    }

    /**
     * 获取job
     *
     * @param param
     * @return
     */
//    public List<JobModel> getByList(JobParam param) {
//        List<Job> jobs = jobDao.get(param);
//        return jobs.stream().map((it) -> {
//            createLog(it);
//            return toModel(it);
//        }).collect(Collectors.toList());
//    }

    /**
     * 查询
     *
     * @param param
     * @return
     */
    public JobModel get(JobParam param) {
        Job job = jobDao.get(param);
        if (job != null) {
            return toModel(job);
        }
        return null;


//        List<Job> jobs = jobDao.get(param);
//        return jobs.stream().map((it) -> {
//            createLog(it);
//            return toModel(it);
//        }).collect(Collectors.toList());
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
        model.setTaskId(job.getTask().getId());
        return model;
    }


}
