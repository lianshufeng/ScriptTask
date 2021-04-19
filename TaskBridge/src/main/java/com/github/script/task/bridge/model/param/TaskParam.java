package com.github.script.task.bridge.model.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.github.script.task.bridge.script.Environment;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskParam {

    /**
     * 任务ID
     */
    private String id;

    /**
     * cron表达式，如: 0/10 * * * * * , 每10秒执行一次
     */
    private String cron;

    /**
     * 脚本名称
     */
    private String scriptName;

    /**
     * 脚本的参数,作为参数的时候不需要描述
     */
    private Map<String, Object> parameters;

    /**
     * 脚本的环境
     */
    private Environment environment;

    /**
     * 绑定的设备标识
     */
    private String deviceId;

    /**
     * 过期时间
     */
    private Long timeout;
}
