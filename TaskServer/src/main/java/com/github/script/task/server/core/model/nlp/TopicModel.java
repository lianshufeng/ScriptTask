package com.github.script.task.server.core.model.nlp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主题模型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicModel {

    //一级标签
    private String[] lv1_tag_list;

    //二级标签
    private String[] lv2_tag_list;

}
