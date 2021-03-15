package top.dzurl.task.bridge.script;

import lombok.Getter;

/**
 * Action的父类
 */
public abstract class SuperScriptAction {

    //当前的脚本
    @Getter
    protected SuperScript script;


    /**
     * 初始化完后执行
     */
    protected void after() {

    }


}
