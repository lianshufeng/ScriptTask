package com.github.script.task.server.core.domain;

import com.github.script.task.server.other.mongo.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 用于检索的标签
 */
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPlatform extends SuperEntity {

    //标签名
    @Indexed(unique = true)
    private String platform;

    //出现次数
    @Indexed
    private Long count;


    //过期时间
    @Indexed(expireAfterSeconds = 0)
    private Date TTL;


}
