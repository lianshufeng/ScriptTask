package com.github.script.task.server.core.dao.mongo;

import com.github.script.task.server.core.dao.mongo.extend.SearchPlatformDaoExtend;
import com.github.script.task.server.core.domain.SearchPlatform;
import com.github.script.task.server.other.mongo.dao.MongoDao;

import java.util.List;

public interface SearchPlatformDao extends MongoDao<SearchPlatform>, SearchPlatformDaoExtend {


}
