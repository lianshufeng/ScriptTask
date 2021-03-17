package com.github.script.task.bridge.script;

import com.github.script.task.bridge.helper.ScriptEventHelper;
import com.github.script.task.bridge.helper.SpringBeanHelper;
import groovy.lang.Script;
import lombok.Getter;
import lombok.SneakyThrows;
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

    //异步方法
    @Getter
    protected ScriptAsync async;

    @Autowired
    private SpringBeanHelper springBeanHelper;


    @Autowired
    private ScriptEventHelper scriptEventHelper;


    //创建脚本的时间
    @Getter
    private long createTime = System.currentTimeMillis();


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
     * 构建Action
     *
     * @param <T>
     * @return
     */
    @SneakyThrows
    public <T extends SuperScriptAction> T action(Class<T> actionClass) {
        Assert.notNull(actionClass, "Action不能为空");
        Constructor constructor = actionClass.getConstructor(null);
        SuperScriptAction superAction = (SuperScriptAction) constructor.newInstance(null);
        superAction.script = this;
        this.springBeanHelper.injection(superAction);
        superAction.after();
        return (T) superAction;
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


}
