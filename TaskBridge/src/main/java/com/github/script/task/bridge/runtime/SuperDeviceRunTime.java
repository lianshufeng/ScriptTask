package com.github.script.task.bridge.runtime;

import com.github.script.task.bridge.device.type.DeviceType;
import com.github.script.task.bridge.script.ScriptRuntime;

/**
 * 设备运行环境
 */
public abstract class SuperDeviceRunTime {

    /**
     * 设备类型
     *
     * @return
     */
    public abstract DeviceType deviceType();


    /**
     * 创建环境
     *
     * @return
     */
    public abstract void create(ScriptRuntime runtime);


    /**
     * 关闭环境
     */
    public abstract void close(ScriptRuntime runtime);
}
