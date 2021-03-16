package com.github.script.task.server.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import com.github.script.task.bridge.device.type.DeviceType;
import com.github.script.task.bridge.script.Environment;
import com.github.script.task.server.other.timer.domain.SimpleTaskTimerTable;

import java.util.Map;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task extends SimpleTaskTimerTable {

    //脚本名
    @Indexed
    private String scriptName;

    //脚本的参数,作为参数的时候不需要描述
    private Map<String, Object> parameters;

    //脚本的环境
    private Environment environment;

    //绑定的设备标识
    @Indexed
    private String deviceId;

    //执行设备
    private DeviceType deviceType;


}
