package com.github.script.task.bridge.script.action.device;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.script.task.bridge.runtime.impl.AndroidSimulatorDeviceRunTime;
import com.github.script.task.bridge.util.ADBUtil;
import com.github.script.task.bridge.util.LeiDianSimulatorUtil;

/**
 * 模拟器功能
 */
@Slf4j
public class SimulatorDeviceAction extends AndroidDeviceAction {

    @Autowired
    private AndroidSimulatorDeviceRunTime androidSimulatorDeviceRunTime;


    /**
     * 模拟器设置坐标
     *
     * @param lng
     * @param lat
     * @return
     */
    @Override
    public boolean setLocation(String lng, String lat) {
        final AndroidSimulatorDeviceRunTime.RunningSimulator runningSimulator = androidSimulatorDeviceRunTime.queryRunningSimulator(super.getScript());
        LeiDianSimulatorUtil.locate(scriptTaskConf.getRunTime().getSimulator(), runningSimulator.getSimulatorName(), lng, lat);
        return true;
    }


    /**
     * 关闭当前模拟器
     */
    public void closeSimulator() {
        final AndroidSimulatorDeviceRunTime.RunningSimulator runningSimulator = androidSimulatorDeviceRunTime.queryRunningSimulator(super.script);
        LeiDianSimulatorUtil.quit(scriptTaskConf.getRunTime().getSimulator(), runningSimulator.getSimulatorName());
    }


    /**
     * 获取
     * @return
     */
    @Override
    public String getMacAddress() {
        AndroidSimulatorDeviceRunTime.RunningSimulator runningSimulator = androidSimulatorDeviceRunTime.queryRunningSimulator(super.script);
        return ADBUtil.getMac(scriptTaskConf.getRunTime().getADBHome(), runningSimulator.getAdbConnectionName());
    }

}
