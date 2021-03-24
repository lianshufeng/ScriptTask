package com.github.script.task.server.core.domain;

import com.github.script.task.server.other.mongo.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndexes({
        //name：索引名称 def：字段(1正序 -1倒序) unique：是否唯一索引
        //直接加到字段上面没用
        @CompoundIndex(name = "match_word_unique", def = "{collectionName:-1, keyWord:-1}", unique = true)
})
public class MatchWord extends SuperEntity {

    //集合名
    @Indexed
    private String collectionName ;

    //关键词
    @Indexed
    private String keyWord;

    //权重值
    private long weightValue;





}
