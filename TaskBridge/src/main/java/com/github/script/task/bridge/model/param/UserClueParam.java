package com.github.script.task.bridge.model.param;

import lombok.Data;

import java.util.Set;

@Data
public class UserClueParam {

    //平台名
    private String platform;

    //用户标识
    private String user;

    //匹配词库
    private Set<String> MatchWordIds;

    //权重值
    private long weightValue;

    private long timeOut;
}
