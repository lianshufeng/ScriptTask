package top.dzurl.task.server.core.dao;

import top.dzurl.task.server.core.dao.extend.JobDaoExtend;
import top.dzurl.task.server.core.domain.Job;
import top.dzurl.task.server.other.mongo.dao.MongoDao;

public interface JobDao extends MongoDao<Job>, JobDaoExtend {
}
