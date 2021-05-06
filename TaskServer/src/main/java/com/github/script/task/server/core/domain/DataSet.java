package com.github.script.task.server.core.domain;

import com.github.script.task.server.core.model.nlp.NLPModel;
import com.github.script.task.server.other.mongo.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataSet extends SuperEntity {

    //数据摘要,去除重复,MD5,小写
    @Indexed(unique = true)
    private String hash;

    //内容
    private String content;

    //自然语言处理模型
    private NLPModel nlp;


}
