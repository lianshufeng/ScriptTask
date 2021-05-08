package com.github.script.task.server.core.dao.mongo;

import com.github.script.task.server.core.dao.mongo.extend.TaskDaoExtend;
import com.github.script.task.server.core.domain.Task;
import com.github.script.task.server.other.mongo.dao.MongoDao;

public interface TaskDao extends MongoDao<Task>, TaskDaoExtend {


}
