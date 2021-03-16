package com.github.script.task.server.core.dao;

import com.github.script.task.server.core.dao.extend.TaskDaoExtend;
import com.github.script.task.server.core.domain.Task;
import com.github.script.task.server.other.mongo.dao.MongoDao;

public interface TaskDao extends MongoDao<Task>, TaskDaoExtend {
}
