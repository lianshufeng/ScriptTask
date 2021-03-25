package com.github.script.task.server.core.domain;

import com.github.script.task.server.other.mongo.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserClue extends SuperEntity {

    //平台名
    @Indexed
    private String platform;

    //用户标识
    private String user;

    //匹配词库
    @DBRef(lazy = true)
    private List<MatchWord> matchWords;

    //权重值
    @Indexed
    private long weightValue;

    @Indexed(expireAfterSeconds = 0)
    private Date ttl;





}
