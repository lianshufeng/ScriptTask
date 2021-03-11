package top.dzurl.task.server.core.dao;

import top.dzurl.task.server.core.dao.extend.JobRunLogDaoExtend;
import top.dzurl.task.server.core.domain.JobRunLog;
import top.dzurl.task.server.other.mongo.dao.MongoDao;

public interface JobRunLogDao extends MongoDao<JobRunLog>, JobRunLogDaoExtend {

    JobRunLog findByJobId(String jobId);
}
