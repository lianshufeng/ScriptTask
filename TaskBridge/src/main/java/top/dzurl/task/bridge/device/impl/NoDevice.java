package top.dzurl.task.bridge.device.impl;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.dzurl.task.bridge.device.Device;
import top.dzurl.task.bridge.device.type.DeviceType;


@Data
@Builder
@NoArgsConstructor
public class NoDevice extends Device {


    @Override
    public DeviceType getType() {
        return DeviceType.None;
    }
}
