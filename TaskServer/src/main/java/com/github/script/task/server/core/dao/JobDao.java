package com.github.script.task.server.core.dao;

import com.github.script.task.server.core.dao.extend.JobDaoExtend;
import com.github.script.task.server.core.domain.Job;
import com.github.script.task.server.other.mongo.dao.MongoDao;

public interface JobDao extends MongoDao<Job>, JobDaoExtend {
}
