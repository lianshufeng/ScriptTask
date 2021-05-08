package com.github.script.task.server.core.dao.mongo.extend;

import com.github.script.task.bridge.model.ClientModel;

public interface ClientDaoExtend {


    /**
     * 更新客户端
     * @param clientModel
     */
    boolean updateClient(ClientModel clientModel);

}
