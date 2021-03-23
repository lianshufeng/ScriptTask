package com.github.script.task.server.core.dao;

import com.github.script.task.server.core.dao.extend.MatchWordDaoExtend;
import com.github.script.task.server.core.domain.MatchWord;
import com.github.script.task.server.other.mongo.dao.MongoDao;

import java.util.List;
import java.util.Set;

public interface MatchWordDao extends MongoDao<MatchWord> , MatchWordDaoExtend {

    List<MatchWord> findByCollectionNameIn(Set<String> collectionName);

    boolean existsByCollectionNameAndKeyWord(String collectionName,String keyWord);
}
