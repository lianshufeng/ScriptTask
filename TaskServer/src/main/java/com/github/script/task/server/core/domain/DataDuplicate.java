package com.github.script.task.server.core.domain;

import com.github.script.task.server.other.mongo.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndexes({
        //name：索引名称 def：字段(1正序 -1倒序) unique：是否唯一索引
        //直接加到字段上面没用
        @CompoundIndex(name = "data_duplicate", def = "{scriptName:-1, value:-1}", unique = true)
})
public class DataDuplicate extends SuperEntity {

    //脚本名称
    @Indexed
    private String scriptName;

    //值
    @Indexed
    private String value;

    //TTL
    @Indexed(expireAfterSeconds = 0)
    private Date ttl;
}
