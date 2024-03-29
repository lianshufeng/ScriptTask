package com.github.script.task.bridge.model;

import lombok.Data;

import java.util.Set;

@Data
public class UserClueModel {

    private String id;

    //平台名
    private String platform;

    //用户标识
    private String user;

    //匹配词库
    private Set<MatchWordModel> matchWords;

    //权重值
    private long weightValue;

    //创建时间
    private Long createTime;

    //匹配信息
    private String matchInfo;

    private String remark;
}
