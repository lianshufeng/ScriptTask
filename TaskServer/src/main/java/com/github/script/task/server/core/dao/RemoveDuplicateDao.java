package com.github.script.task.server.core.dao;

import com.github.script.task.server.core.domain.RemoveDuplicate;
import com.github.script.task.server.other.mongo.dao.MongoDao;

import java.util.List;

public interface RemoveDuplicateDao extends MongoDao<RemoveDuplicate> {

    List<RemoveDuplicate> findByScriptNameAndValueIn(String scriptName,List<String> value);
}
