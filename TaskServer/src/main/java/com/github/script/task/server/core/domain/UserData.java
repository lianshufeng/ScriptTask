package com.github.script.task.server.core.domain;

import com.github.script.task.server.other.mongo.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndexes({
        //name：索引名称 def：字段(1正序 -1倒序) unique：是否唯一索引
        @CompoundIndex(name = "user_content_unique", def = "{user:-1, contentHash:-1}", unique = true)
})
public class UserData extends SuperEntity {

    //用户标识
    @Indexed
    private String user;

    /**
     * hash
     * {@link DataSet}
     */
    @Indexed
    private String contentHash;


    //是否删除该数据
    private boolean del;


    //过期时间
    @Indexed(expireAfterSeconds = 0)
    private Date TTL;


}
