package top.dzurl.task.server.core.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.dzurl.task.bridge.model.JobModel;
import top.dzurl.task.bridge.model.param.JobParam;
import top.dzurl.task.bridge.result.ResultContent;
import top.dzurl.task.bridge.result.ResultState;
import top.dzurl.task.server.core.dao.JobDao;
import top.dzurl.task.server.core.dao.JobRunLogDao;
import top.dzurl.task.server.core.domain.Job;
import top.dzurl.task.server.core.domain.JobRunLog;
import top.dzurl.task.server.other.mongo.helper.DBHelper;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobDao jobDao;

    @Autowired
    private JobRunLogDao jobRunLogDao;

    @Autowired
    private DBHelper dbHelper;

    /**
     * 获取job
     * @param deviceId
     * @return
     */
    public JobModel get(String deviceId){
        Job job = jobDao.get(deviceId);
        createLog(job,deviceId);
        return toModel(job);
    }

    /**
     * 创建日志
     * @param job
     */
    private void createLog(Job job,String deviceId){
        if (job != null){
            JobRunLog log = new JobRunLog();
            log.setScriptName(job.getScriptName());
            log.setGetJobTime(dbHelper.getTime());
            log.setDeviceId(deviceId);
            log.setJobId(job.getId());
            jobRunLogDao.save(log);
        }
    }

    /**
     * 写工作日志
     * @param param
     * @return
     */
    public ResultContent<String> writeLog(JobParam param){
        JobRunLog jobRunLog = jobRunLogDao.findByJobId(param.getJobId());
        if (jobRunLog == null){
            return ResultContent.build(ResultState.JobLogNoneExists);
        }
        List<String> logs = jobRunLog.getLogs();
        if (CollectionUtils.isEmpty(logs)){
            logs = new ArrayList<>();
        }
        logs.addAll(param.getLogs());
        jobRunLog.setLogs(logs);
        jobRunLogDao.save(jobRunLog);
        return ResultContent.build(ResultState.Success);
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
