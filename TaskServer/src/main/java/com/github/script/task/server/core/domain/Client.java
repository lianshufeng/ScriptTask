package com.github.script.task.server.core.domain;

import com.github.script.task.bridge.device.type.DeviceType;
import com.github.script.task.server.other.mongo.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 客户机列表
 */
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client extends SuperEntity {


    //uuid
    @Indexed
    private String uuid;

    //系统名
    @Indexed
    private String osName;

    //系统版本
    @Indexed
    private String osVersion;

    //可以执行设备类型
    @Indexed
    private DeviceType[] deviceTypes;

    //过期时间
    @Indexed(expireAfterSeconds = 0)
    private Date ttl;

}
