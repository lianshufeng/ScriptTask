package com.github.script.task.bridge.runtime.impl;

import com.github.script.task.bridge.device.type.DeviceType;
import com.github.script.task.bridge.runtime.SuperDeviceRunTime;
import com.github.script.task.bridge.script.ScriptRuntime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * android真机
 */
@Slf4j
@Component
public class NoneDeviceRunTime extends SuperDeviceRunTime {


    @Override
    public void create(ScriptRuntime runtime) {
        //nothing
    }

    @Override
    public void close(ScriptRuntime runtime) {
        //nothing
    }

    @Override
    public DeviceType deviceType() {
        return DeviceType.None;
    }
}
