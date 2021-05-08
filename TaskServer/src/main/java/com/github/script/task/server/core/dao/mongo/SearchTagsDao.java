package com.github.script.task.server.core.dao.mongo;

import com.github.script.task.server.core.dao.mongo.extend.SearchTagsDaoExtend;
import com.github.script.task.server.core.domain.SearchTags;
import com.github.script.task.server.other.mongo.dao.MongoDao;

public interface SearchTagsDao extends MongoDao<SearchTags>, SearchTagsDaoExtend {

}
