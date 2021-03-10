package top.dzurl.task.client.core.runtime;

import top.dzurl.task.bridge.device.type.DeviceType;
import top.dzurl.task.bridge.script.ScriptRuntime;

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
