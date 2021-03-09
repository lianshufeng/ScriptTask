package top.dzurl.task.client.core.runtime.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.dzurl.task.bridge.device.type.DeviceType;
import top.dzurl.task.bridge.script.ScriptRuntime;
import top.dzurl.task.client.core.runtime.SuperDeviceRunTime;

/**
 * android真机
 */
@Slf4j
@Component
public class NoneDeviceRunTime extends SuperDeviceRunTime {


    @Override
    public void open(ScriptRuntime runtime) {

    }

    @Override
    public void close(ScriptRuntime runtime) {

    }

    @Override
    public DeviceType deviceType() {
        return DeviceType.None;
    }
}
