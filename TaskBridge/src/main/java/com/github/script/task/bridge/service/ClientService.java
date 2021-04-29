package com.github.script.task.bridge.service;

import com.github.script.task.bridge.model.ClientModel;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientService extends SuperService {

    /**
     * 心跳
     *
     * @param clientModel
     */
    public void heartbeat(ClientModel clientModel) {
        try {
            ResultContent resultContent = postJson("/client/heartbeat", clientModel, ResultContent.class);
            if (resultContent.getState() != ResultState.Success) {
                log.info("客户端心跳失败 ,状态 : {} , 原因 :{}", resultContent.getState(), resultContent.getContent());
            }
        } catch (Exception e) {
            log.error("e : {}", e.getMessage());
        }
    }

}
