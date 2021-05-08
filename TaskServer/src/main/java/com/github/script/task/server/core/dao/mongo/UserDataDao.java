package com.github.script.task.server.core.dao.mongo;

import com.github.script.task.server.core.dao.mongo.extend.UserDataDaoExtend;
import com.github.script.task.server.core.domain.UserData;
import com.github.script.task.server.other.mongo.dao.MongoDao;

public interface UserDataDao extends MongoDao<UserData>, UserDataDaoExtend {


    /**
     * 是否存在该记录
     *
     * @return
     */
    boolean existsByPlatformAndUserAndContentHash(String platform, String user, String contentHash);

}
