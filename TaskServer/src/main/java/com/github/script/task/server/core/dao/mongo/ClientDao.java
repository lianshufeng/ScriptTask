package com.github.script.task.server.core.dao.mongo;

import com.github.script.task.server.core.dao.mongo.extend.ClientDaoExtend;
import com.github.script.task.server.core.domain.Client;
import com.github.script.task.server.other.mongo.dao.MongoDao;

public interface ClientDao extends MongoDao<Client>, ClientDaoExtend {


}
