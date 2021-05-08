package com.github.script.task.server.core.dao.mongo;

import com.github.script.task.server.core.domain.DataDuplicate;
import com.github.script.task.server.other.mongo.dao.MongoDao;

import java.util.List;

public interface DataDuplicateDao extends MongoDao<DataDuplicate> {

    List<DataDuplicate> findByKeyAndValueIn(String scriptName, List<String> value);
}
