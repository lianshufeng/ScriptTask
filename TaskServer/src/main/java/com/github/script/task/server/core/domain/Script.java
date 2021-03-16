package com.github.script.task.server.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import com.github.script.task.bridge.device.type.DeviceType;
import com.github.script.task.bridge.script.Environment;
import com.github.script.task.bridge.script.Parameter;
import com.github.script.task.server.other.mongo.domain.SuperEntity;

import java.util.Map;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Script extends SuperEntity {

    //脚本名
    @Indexed(unique = true)
    private String name;

    //脚本的备注
    private String remark;

    //脚本的参数
    private Map<String, Parameter> parameters;

    //脚本的环境
    private Environment environment;

    //脚本的md5
    @Indexed(unique = true)
    private String hash;

    //脚本内容
    private byte[] body;

    //执行设备
    private DeviceType deviceType;


}
