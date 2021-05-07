package com.github.script.task.server.core.dao;

import com.github.script.task.server.core.dao.extend.UserDataDaoExtend;
import com.github.script.task.server.core.domain.UserData;
import com.github.script.task.server.other.mongo.dao.MongoDao;

public interface UserDataDao extends MongoDao<UserData>, UserDataDaoExtend {


    /**
     * 是否存在该记录
     *
     * @param user
     * @param contentHash
     * @return
     */
    boolean existsByUserAndContentHash(String user, String contentHash);


}
