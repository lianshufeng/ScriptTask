package com.github.script.task.bridge.model.param;

import lombok.Data;

import java.util.Set;

@Data
public class UserClueParam {

    private String id;

    //平台名
    private String platform;

    //用户标识
    private String user;

    //匹配词库
    private Set<String> MatchWordIds;

    //权重值
    private long weightValue;

    private long timeOut;

    private String matchInfo;

    private String remark;
}
