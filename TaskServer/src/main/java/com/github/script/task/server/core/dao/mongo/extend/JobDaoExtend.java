package com.github.script.task.server.core.dao.mongo.extend;

import com.github.script.task.server.core.domain.Job;
import com.github.script.task.bridge.model.param.JobParam;

public interface JobDaoExtend {

    Job get(JobParam param);

//    List<Job> get(JobParam param);

    Job resetDeice();

    Job updateJobCreatTime(String jobId, Long createTime);
}
