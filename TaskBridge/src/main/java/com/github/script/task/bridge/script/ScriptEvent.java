package com.github.script.task.bridge.script;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

/**
 * 脚本事件
 */
public abstract class ScriptEvent {

    /**
     * 创建环境
     */
    public void onCreate() {
    }

    /**
     * 执行
     */
    public void onRun() {
    }

    /**
     * 关闭环境
     */
    public void onClose() {
    }


    /**
     * 脚本被终止
     */
    public void onInterrupt() {

    }

    /**
     * 异常
     *
     * @param e
     */
    public void onException(Exception e) {
    }


    /**
     * 事件对应的枚举类
     */
    public static enum EventType {
        Create(getMethod("onCreate", null)),
        Run(getMethod("onRun", null)),
        Close(getMethod("onClose", null)),
        onInterrupt(getMethod("onInterrupt", null)),
        Exception(getMethod("onException", Exception.class)),


        ;

        EventType(Method method) {
            this.method = method;
        }

        @Getter
        private Method method;


        @SneakyThrows
        private static Method getMethod(String methodName, Class<?>... parameterTypes) {
            return ScriptEvent.class.getMethod(methodName, parameterTypes);
        }

    }

}