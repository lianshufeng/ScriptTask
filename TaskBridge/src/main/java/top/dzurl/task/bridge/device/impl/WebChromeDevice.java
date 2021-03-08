package top.dzurl.task.bridge.device.impl;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.dzurl.task.bridge.device.Device;
import top.dzurl.task.bridge.device.type.DeviceType;


@Data
@NoArgsConstructor
public class WebChromeDevice extends Device {


    @Override
    public DeviceType getType() {
        return DeviceType.Web;
    }
}
