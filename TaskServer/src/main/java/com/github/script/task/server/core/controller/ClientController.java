package com.github.script.task.server.core.controller;

import com.github.script.task.bridge.model.ClientModel;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.server.core.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * 查询所有的客户端
     *
     * @param pageable
     * @return
     */
    @RequestMapping("list")
    public Object list(Pageable pageable) {
        return ResultContent.buildContent(clientService.list(pageable));
    }

    @RequestMapping("heartbeat")
    public Object heartbeat(@RequestBody ClientModel clientModel) {
        return ResultContent.build(clientService.heartbeat(clientModel));
    }


}
