package com.github.script.task.server.core.dao.mongo;

import com.github.script.task.server.core.dao.mongo.extend.MatchWordDaoExtend;
import com.github.script.task.server.core.domain.MatchWord;
import com.github.script.task.server.other.mongo.dao.MongoDao;

import java.util.List;

public interface MatchWordDao extends MongoDao<MatchWord>, MatchWordDaoExtend {

    List<MatchWord> findByCollectionNameIn(List<String> collectionName);

    boolean existsByCollectionNameAndKeyWord(String collectionName, String keyWord);

}
