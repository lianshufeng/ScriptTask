package com.github.script.task.bridge.runtime.impl;

import com.github.script.task.bridge.conf.ScriptTaskConf;
import com.github.script.task.bridge.device.type.DeviceType;
import com.github.script.task.bridge.runtime.SuperDeviceRunTime;
import com.github.script.task.bridge.script.ScriptRuntime;
import com.github.script.task.bridge.script.SuperScript;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Vector;

/**
 * android真机
 */
@Slf4j
@Component
public class AndroidMachineDeviceRunTime extends SuperDeviceRunTime {

    //正在运行的虚拟机
    protected Vector<AndroidMachineDeviceRunTime.RunningSimulator> runningSimulators = new Vector<>();

    @Autowired
    protected ScriptTaskConf scriptTaskConf;


    /**
     * 通过脚本查询正在运行的模拟器信息
     *
     * @param script
     * @return
     */
    public AndroidSimulatorDeviceRunTime.RunningSimulator queryRunningSimulator(SuperScript script) {
        if (script == null) {
            return null;
        }
        for (AndroidSimulatorDeviceRunTime.RunningSimulator runningSimulator : runningSimulators) {
            SuperScript runningSimulatorScript = runningSimulator.getScript();
            if (runningSimulatorScript != null && runningSimulatorScript.equals(script)) {
                return runningSimulator;
            }
        }
        return null;
    }


    @Override
    public void create(ScriptRuntime runtime) {

    }

    @Override
    public void close(ScriptRuntime runtime) {

    }

    @Override
    public DeviceType deviceType() {
        return DeviceType.AndroidMachine;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RunningSimulator {
        //模拟器名称
        private String simulatorName;

        //取出adb的androidid
        private String adbConnectionName;

        //模拟器对应的本地服务
        private AppiumDriverLocalService appiumDriverLocalService;

        //是否正在工作
        private boolean working;

        //最后一次访问时间
        private long lastAccessTime;

        //创建时间
        private long createTime;

        //android的驱动
        private AndroidDriver driver;

        //当前正在执行的脚本
        private SuperScript script;

    }
}
