package top.dzurl.task.bridge.script;

import groovy.lang.Script;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 脚本的父类
 */
public abstract class SuperScript extends Script {

    protected static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SuperScript.class);


    @Getter
    @Setter
    protected ScriptRuntime runtime;

    //异步方法
    @Getter
    @Setter
    protected ScriptAsync async;


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
     * 执行方法
     *
     * @return
     */
    public final Object execute() {
        return this.run();
    }


}
