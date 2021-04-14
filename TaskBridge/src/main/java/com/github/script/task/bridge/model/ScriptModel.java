package com.github.script.task.bridge.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.script.task.bridge.script.Environment;
import com.github.script.task.bridge.script.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScriptModel {

    //脚本名
    private String name;

    //脚本的备注
    private String remark;

    //脚本的参数
    private Map<String, Parameter> parameters;

    //脚本的环境
    private Environment environment;

    //脚本的md5
    private String hash;

    //脚本内容
    private byte[] body;

    //更新时间
    private long updateTime;

}
