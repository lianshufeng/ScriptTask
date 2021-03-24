package com.github.script.task.server.core.dao;

import com.github.script.task.server.core.dao.extend.UserClueDaoExtend;
import com.github.script.task.server.core.domain.UserClue;
import com.github.script.task.server.other.mongo.dao.MongoDao;

public interface UserClueDao extends MongoDao<UserClue>, UserClueDaoExtend {

}
