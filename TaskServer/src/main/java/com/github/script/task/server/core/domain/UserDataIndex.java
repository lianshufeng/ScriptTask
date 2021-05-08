package com.github.script.task.server.core.domain;

import com.github.script.task.server.other.es.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "userdata")
@Setting(settingPath = "es/UserDataIndex.json")
public class UserDataIndex extends SuperEntity {

    //数据id
    @Field(type = FieldType.Keyword)
    private String userDataId;

    //平台标识
    @Field(analyzer = "ik_smart", searchAnalyzer = "ik_max_word", type = FieldType.Text)
    private String platform;

    //用户标识
    @Field(analyzer = "ik_smart", searchAnalyzer = "ik_max_word", type = FieldType.Text)
    private String user;


    //索引内容
    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", type = FieldType.Text)
    private String content;


    //摘要
    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", type = FieldType.Text)
    private String summary;


    //标签
    @Field(type = FieldType.Keyword)
    private Set<String> tags;


    //权重值
    private int weight;


}
