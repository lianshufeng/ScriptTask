package com.github.script.task.bridge.device.impl;

import com.github.script.task.bridge.device.Device;
import com.github.script.task.bridge.device.type.DeviceType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
public class NoDevice extends Device {


    @Override
    public DeviceType getType() {
        return DeviceType.None;
    }
}
