package com.github.script.task.server.core.dao.mongo;

import com.github.script.task.server.core.dao.mongo.extend.UserClueDaoExtend;
import com.github.script.task.server.core.domain.UserClue;
import com.github.script.task.server.other.mongo.dao.MongoDao;

public interface UserClueDao extends MongoDao<UserClue>, UserClueDaoExtend {
    boolean existsByUser(String user);
}
