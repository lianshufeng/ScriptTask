package com.github.script.task.bridge.service;

import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SuperService {

    private interface BridgeMethod {
        public <T> T postForm(String uri, Object parameter, Class<T> retCls);

        public <T> T postJson(String uri, Object parameter, Class<T> retCls);
    }


    @Autowired
    @Delegate(types = BridgeMethod.class)
    private TaskBridgeService taskServiceBridge;


}
