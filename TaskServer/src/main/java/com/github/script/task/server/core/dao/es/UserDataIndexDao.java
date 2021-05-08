package com.github.script.task.server.core.dao.es;

import com.github.script.task.server.core.dao.es.extend.UserDataIndexDaoExtend;
import com.github.script.task.server.core.domain.UserDataIndex;
import com.github.script.task.server.other.es.dao.ElasticSearchDao;

public interface UserDataIndexDao extends ElasticSearchDao<UserDataIndex>, UserDataIndexDaoExtend {
}
