package com.github.script.task.bridge.model;

import com.github.script.task.bridge.device.type.DeviceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientModel {


    //uuid
    private String uuid;

    //系统名
    private String osName;

    //系统版本
    private String osVersion;

    //可以执行设备类型
    private DeviceType[] deviceTypes;

    //创建时间
    private long createTime;

}
