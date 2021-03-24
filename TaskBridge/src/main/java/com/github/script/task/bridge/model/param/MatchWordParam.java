package com.github.script.task.bridge.model.param;

import lombok.Data;

@Data
public class MatchWordParam {

    private String id;

    //集合名
    private String collectionName ;

    //关键词
    private String keyWord;

    //权重值
    private long weightValue;

}
