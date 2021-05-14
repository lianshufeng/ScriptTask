package com.github.script.task.server.core.dao.mongo.extend;

import java.util.Map;

public interface SearchPlatformDaoExtend {


    /**
     * 增加ttl时间
     *
     * @param tags
     */
    void update(Map<String, Long> tags);


}
