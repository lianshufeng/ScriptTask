package com.github.script.task.bridge.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.script.task.bridge.device.type.DeviceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobModel extends ScriptRunTimeModel {

    //脚本名
    private String scriptName;

    //执行设备
    private DeviceType deviceType;

}
