package top.dzurl.task.server.core.dao;

import top.dzurl.task.server.core.dao.extend.TaskDaoExtend;
import top.dzurl.task.server.core.domain.Task;
import top.dzurl.task.server.other.mongo.dao.MongoDao;

public interface TaskDao extends MongoDao<Task>, TaskDaoExtend {
}
