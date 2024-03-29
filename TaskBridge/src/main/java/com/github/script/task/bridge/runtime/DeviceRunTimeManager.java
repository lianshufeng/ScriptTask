package com.github.script.task.bridge.runtime;

import com.github.script.task.bridge.device.type.DeviceType;
import com.github.script.task.bridge.helper.CurrentStateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import com.github.script.task.bridge.helper.ScriptEventHelper;
import com.github.script.task.bridge.script.ScriptEvent;
import com.github.script.task.bridge.script.ScriptRuntime;
import com.github.script.task.bridge.script.SuperScript;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DeviceRunTimeManager {

    @Autowired
    private ScriptEventHelper scriptEventHelper;

    private Map<DeviceType, SuperDeviceRunTime> _cache = new ConcurrentHashMap<>();


    @Autowired
    private CurrentStateHelper currentStateHelper;


    /**
     * 初始化并缓存设备类型
     *
     * @param applicationContext
     */
    @Autowired
    private void init(ApplicationContext applicationContext) {
        applicationContext.getBeansOfType(SuperDeviceRunTime.class).values().forEach((it) -> {
            _cache.put(it.deviceType(), it);
        });
    }


    /**
     * 创建环境
     *
     * @return
     */
    public void create(SuperScript script) {
        Optional.ofNullable(script.getRuntime().getEnvironment().getDevice()).ifPresent((it) -> {
            currentStateHelper.removePower(it.getType());
        });
        //删除此能力
        executeEvent(script, ScriptEvent.EventType.Create);
    }


    /**
     * 关闭环境
     */
    public void close(SuperScript script) {
        executeEvent(script, ScriptEvent.EventType.Close);
        currentStateHelper.addPower(script.environment().getDevice().getType());
    }


    /**
     * 执行事件对应的方法
     *
     * @param script
     * @param eventType
     */
    private void executeEvent(SuperScript script, ScriptEvent.EventType eventType) {
        try {
            final ScriptRuntime scriptRunTime = script.getRuntime();
            final SuperDeviceRunTime deviceRunTime = getRunTime(scriptRunTime);
            this.scriptEventHelper.publish(script, eventType);
            if (eventType == ScriptEvent.EventType.Create) {
                deviceRunTime.create(scriptRunTime);
            } else if (eventType == ScriptEvent.EventType.Close) {
                deviceRunTime.close(scriptRunTime);
            }
        } catch (Exception e) {
            this.scriptEventHelper.publish(script, ScriptEvent.EventType.Exception, e);
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }


    /**
     * 取出运行环境
     *
     * @param runtime
     * @return
     */
    private SuperDeviceRunTime getRunTime(ScriptRuntime runtime) {
        return _cache.get(runtime.getEnvironment().getDevice().getType());
    }

}
