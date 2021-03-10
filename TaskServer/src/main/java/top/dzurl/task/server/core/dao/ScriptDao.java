package top.dzurl.task.server.core.dao;

import top.dzurl.task.server.core.dao.extend.ScriptDaoExtend;
import top.dzurl.task.server.core.domain.Script;
import top.dzurl.task.server.other.mongo.dao.MongoDao;

public interface ScriptDao extends MongoDao<Script>, ScriptDaoExtend {

    Script findByName(String name);

    boolean existsByName(String name);


}
