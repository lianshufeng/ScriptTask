package com.github.script.task.server.core.model.nlp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 语言处理应用模型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NLPModel {

    //内容摘要
    private String summary;

    //标题
    private String title;

    //关键词
    private String[] keyword;

    //文章主题(分类)
    private TopicModel topic;


}
