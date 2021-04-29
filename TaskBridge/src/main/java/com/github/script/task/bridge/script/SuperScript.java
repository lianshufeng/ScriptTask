package com.github.script.task.bridge.script;

import com.github.script.task.bridge.helper.ScriptEventHelper;
import groovy.lang.Script;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * 脚本的父类
 */
public abstract class SuperScript extends Script {

    protected ScriptLog log;

    @Getter
    protected ScriptRuntime runtime;

    @Autowired
    private ScriptEventHelper scriptEventHelper;

    @Autowired
    private ActionFactory actionFactory;

    //创建脚本的时间
    @Getter
    private long createTime = System.currentTimeMillis();


    private interface ScriptTimoutProxy {
        //心跳
        public void heartbeat();
    }

    //脚本超时
    @Delegate(types = {ScriptTimoutProxy.class})
    protected ScriptTimout scriptTimout;

    /**
     * 脚本名称
     *
     * @return
     */
    public abstract String name();


    /**
     * 环境
     *
     * @return
     */
    public Environment environment() {
        return Environment.builder().build();
    }

    /**
     * 参数
     *
     * @return
     */
    public Map<String, Parameter> parameters() {
        return new HashMap<>();
    }


    /**
     * 脚本事件
     *
     * @return
     */
    public ScriptEvent event() {
        return null;
    }


    /**
     * 脚本描述
     *
     * @return
     */
    public String remark() {
        return String.format("[%s] 脚本", this.name());
    }


    /**
     * 执行方法
     *
     * @return
     */
    public final Object execute() {
        try {
            this.scriptEventHelper.publish(this, ScriptEvent.EventType.Run);
            return this.run();
        } catch (Exception e) {
            this.scriptEventHelper.publish(this, ScriptEvent.EventType.Exception, e);
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 构建 Action
     *
     * @param actionClass
     * @param <T>
     * @return
     */
    public <T extends SuperScriptAction> T action(Class<T> actionClass) {
        return actionFactory.action(this, actionClass);
    }

}
