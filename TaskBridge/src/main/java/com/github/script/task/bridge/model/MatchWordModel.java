package com.github.script.task.bridge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchWordModel  {

    private String id;

    //集合名
    private String collectionName ;

    //关键词
    private String keyWord;

    //权重值
    private long weightValue;

    private Long createTime;


}
