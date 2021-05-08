package com.github.script.task.server.core.service;

import com.github.script.task.bridge.model.ClientModel;
import com.github.script.task.server.core.dao.mongo.ClientDao;
import com.github.script.task.server.core.domain.Client;
import com.github.script.task.server.other.mongo.util.PageEntityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ClientDao clientDao;


    /**
     * 分页查询
     *
     * @param pageable
     * @return
     */
    public Page<ClientModel> list(Pageable pageable) {
        return PageEntityUtil.concurrent2PageModel(clientDao.findAll(pageable), (client) -> {
            return toModel(client);
        });
    }


    /**
     * 转换到模型
     *
     * @return
     */
    public ClientModel toModel(Client client) {
        ClientModel clientModel = new ClientModel();
        BeanUtils.copyProperties(client, clientModel);
        return clientModel;
    }


    /**
     * 心跳
     *
     * @param clientModel
     * @return
     */
    public boolean heartbeat(ClientModel clientModel) {
        return clientDao.updateClient(clientModel);
    }


}
