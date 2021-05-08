package com.github.script.task.server.core.dao.mongo.extend;

import com.github.script.task.server.core.domain.UserData;

public interface UserDataDaoExtend {

    /**
     * 增加数据
     *
     * @return
     */
    UserData append(String platform, String user, String contentHash);


}
