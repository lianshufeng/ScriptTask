package com.github.script.task.bridge.helper;

import com.github.script.task.bridge.conf.ScriptTaskConf;
import com.github.script.task.bridge.device.type.DeviceType;
import com.github.script.task.bridge.util.LeiDianSimulatorUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

@Component
public class CurrentStateHelper {

    @Autowired
    private ScriptTaskConf scriptTaskConf;

    /**
     * 当前可用的能力
     */
    @Getter
    private Vector<DeviceType> powerType = null;



    @Autowired
    private void init(ApplicationContext applicationContext) {
        //读取配置中的能力值
        ScriptTaskConf.RemoteTask remoteTask = this.scriptTaskConf.getRemoteTask();
        if (remoteTask == null || remoteTask.getPowerType() == null) {
            powerType = new Vector<>(new ScriptTaskConf.RemoteTask().getPowerType());
        } else {
            powerType = new Vector<>(remoteTask.getPowerType());
        }

    }


    /**
     * 删除一个能力值
     *
     * @param power
     */
    public void removePower(DeviceType power) {
        this.powerType.remove(power);
    }

    /**
     * 添加一个能力值
     *
     * @param power
     */
    public void addPower(DeviceType power) {
        this.powerType.add(power);
    }


    /**
     * 获取主机的所有设备id
     *
     * @return
     */
    public Set<String> getDeviceIds() {
        Set<String> deviceIds = new HashSet<>();
        appendSimulatorDevice(deviceIds);
        return deviceIds;
    }

    /**
     * 追加模拟器的设备
     */
    private void appendSimulatorDevice(Set<String> deviceIds) {
        deviceIds.addAll(LeiDianSimulatorUtil.list(scriptTaskConf.getRunTime().getSimulator()).values().stream()
                .filter((it) -> {
                    return it.get(LeiDianSimulatorUtil.SimulatorMacAddress) != null;
                }).map((it) -> {
                    return String.valueOf(it.get(LeiDianSimulatorUtil.SimulatorMacAddress));
                }).collect(Collectors.toSet()));
    }


}
