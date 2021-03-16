package com.github.script.task.server.core.dao;

import com.github.script.task.server.core.dao.extend.JobRunLogDaoExtend;
import com.github.script.task.server.core.domain.JobRunLog;
import com.github.script.task.server.other.mongo.dao.MongoDao;

public interface JobRunLogDao extends MongoDao<JobRunLog>, JobRunLogDaoExtend {

    JobRunLog findByJobId(String jobId);
}
