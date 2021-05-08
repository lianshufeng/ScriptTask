package com.github.script.task.server.core.dao.mongo;

import com.github.script.task.server.core.dao.mongo.extend.ScriptDaoExtend;
import com.github.script.task.server.core.domain.Script;
import com.github.script.task.server.other.mongo.dao.MongoDao;

public interface ScriptDao extends MongoDao<Script>, ScriptDaoExtend {

    Script findByName(String name);

    boolean existsByName(String name);


}
