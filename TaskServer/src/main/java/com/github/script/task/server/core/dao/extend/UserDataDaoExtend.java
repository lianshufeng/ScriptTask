package com.github.script.task.server.core.dao.extend;

public interface UserDataDaoExtend {

    /**
     * 增加数据
     *
     * @param user
     * @param contentHash
     * @return
     */
    String append(String user, String contentHash);


}
