package com.github.script.task.server.core.dao;

import com.github.script.task.server.core.dao.extend.ClientDaoExtend;
import com.github.script.task.server.core.domain.Client;
import com.github.script.task.server.other.mongo.dao.MongoDao;

public interface ClientDao extends MongoDao<Client>, ClientDaoExtend {


}
