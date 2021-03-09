package top.dzurl.task.client.core.runtime.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.dzurl.task.bridge.device.type.DeviceType;
import top.dzurl.task.bridge.script.ScriptRuntime;
import top.dzurl.task.client.core.conf.AppTaskConf;
import top.dzurl.task.client.core.factory.AppiumFactory;
import top.dzurl.task.client.core.runtime.SuperDeviceRunTime;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * android真机
 */
@Slf4j
@Component
public class AndroidMachineDeviceRunTime extends SuperDeviceRunTime {


    //设备mac的缓存
    private Map<String, String> deviceMacCache = new ConcurrentHashMap<>();

    @Autowired
    private AppTaskConf appTaskConf;

    @Autowired
    private AppiumFactory appiumFactory;

    private File ADBHome = null;


    @Override
    public void open(ScriptRuntime runtime) {

    }

    @Override
    public void close(ScriptRuntime runtime) {

    }

    @Override
    public DeviceType deviceType() {
        return DeviceType.AndroidMachine;
    }
}
