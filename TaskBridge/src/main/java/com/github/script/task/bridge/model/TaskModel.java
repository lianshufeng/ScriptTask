package com.github.script.task.bridge.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.github.script.task.bridge.script.Environment;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskModel {

    //定时器
    private String cron;

    //任务ID
    private String id;

    //脚本名
    private String scriptName;

    //脚本的参数,作为参数的时候不需要描述
    private Map<String, Object> parameters;

    //脚本的环境
    private Environment environment;

    //绑定的设备标识
    private String deviceId;
}
