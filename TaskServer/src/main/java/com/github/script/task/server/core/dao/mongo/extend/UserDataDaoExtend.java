package com.github.script.task.server.core.dao.mongo.extend;

import com.github.script.task.server.core.domain.UserData;

import java.util.Map;

public interface UserDataDaoExtend {

    /**
     * 增加数据
     *
     * @return
     */
    UserData append(String platform, String user, String contentHash);


    /**
     * 取出
     * @return
     */
    Map<String, Long> platforms();


}
