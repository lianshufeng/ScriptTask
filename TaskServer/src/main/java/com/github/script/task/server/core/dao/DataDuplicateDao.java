package com.github.script.task.server.core.dao;

import com.github.script.task.server.core.domain.DataDuplicate;
import com.github.script.task.server.other.mongo.dao.MongoDao;

import java.util.List;

public interface DataDuplicateDao extends MongoDao<DataDuplicate> {

    List<DataDuplicate> findByScriptNameAndValueIn(String scriptName, List<String> value);
}
