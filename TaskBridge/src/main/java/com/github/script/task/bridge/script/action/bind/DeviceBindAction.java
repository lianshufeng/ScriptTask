package com.github.script.task.bridge.script.action.bind;

import com.github.script.task.bridge.conf.ScriptTaskConf;
import com.github.script.task.bridge.runtime.impl.AndroidSimulatorDeviceRunTime;
import com.github.script.task.bridge.script.ScriptRuntime;
import com.github.script.task.bridge.script.SuperScriptAction;
import com.github.script.task.bridge.service.TaskService;
import com.github.script.task.bridge.util.LeiDianSimulatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DeviceBindAction extends SuperScriptAction {


    @Autowired
    private ScriptTaskConf scriptTaskConf;

    @Autowired
    private AndroidSimulatorDeviceRunTime androidSimulatorDeviceRunTime;

    @Autowired
    private TaskService taskService;


    /**
     * 绑定
     */
    public void bind() {
        checkAndBind();
    }


    /**
     * 检查或者绑定
     */
    private void checkAndBind() {
        ScriptRuntime scriptRuntime = getScript().getRuntime();
        String deviceId = scriptRuntime.getDeviceId();


        //取出当前正在执行的模拟器
        AndroidSimulatorDeviceRunTime.RunningSimulator runningSimulator = androidSimulatorDeviceRunTime.queryRunningSimulator(script);
        Map<String, Object> simulatorInfo = LeiDianSimulatorUtil.get(scriptTaskConf.getRunTime().getSimulator(), runningSimulator.getSimulatorName());
        String simulatorPlayerName = String.valueOf(simulatorInfo.get(LeiDianSimulatorUtil.SimulatorPlayerName));
        String simulatorMacAddress = String.valueOf(simulatorInfo.get(LeiDianSimulatorUtil.SimulatorMacAddress));


        //检查本地的模拟器是否已经绑定过了
        Object bind = simulatorInfo.get(LeiDianSimulatorUtil.DeviceBindName);
        boolean isDeviceBind = (bind != null && bind instanceof Boolean && bind == Boolean.TRUE);
        if (!isDeviceBind) {
            bindLocalDevice(simulatorPlayerName);
        }


        //模拟器的设备id与任务的设备id不匹配则允许重新绑定
        if (deviceId == null || !deviceId.equals(simulatorMacAddress)) {
            bindRemoteTask(simulatorMacAddress);
        }

    }

    /**
     * 绑定本地设备
     */
    private void bindLocalDevice(String simulatorPlayerName) {
        log.info("[绑定] -[设备]- {}", simulatorPlayerName);
        LeiDianSimulatorUtil.updateExtendConfig(scriptTaskConf.getRunTime().getSimulator(), simulatorPlayerName, new HashMap<String, Object>() {{
            put(LeiDianSimulatorUtil.DeviceBindName, true);
        }});
    }

    /**
     * 绑定远程服务
     */
    private void bindRemoteTask(String deviceMac) {
        ScriptRuntime scriptRuntime = getScript().getRuntime();
        String taskId = scriptRuntime.getTaskId();
        log.info("[绑定] -[任务]- {} -> {}", taskId, deviceMac);
        this.taskService.bindTask(taskId, deviceMac);

    }

}
