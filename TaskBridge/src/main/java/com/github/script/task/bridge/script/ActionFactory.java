package com.github.script.task.bridge.script;

import com.github.script.task.bridge.helper.SpringBeanHelper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;

@Component
public class ActionFactory {

    @Autowired
    private SpringBeanHelper springBeanHelper;

    /**
     * 构建Action
     *
     * @param <T>
     * @return
     */
    @SneakyThrows
    public <T extends SuperScriptAction> T action(SuperScript script, Class<T> actionClass) {
        Assert.notNull(actionClass, "Action不能为空");
        Constructor constructor = actionClass.getConstructor(null);
        SuperScriptAction superAction = (SuperScriptAction) constructor.newInstance(null);
        superAction.script = script;
        this.springBeanHelper.injection(superAction);
        return (T) superAction;
    }


}
