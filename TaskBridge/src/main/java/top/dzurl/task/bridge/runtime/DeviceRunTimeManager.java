package top.dzurl.task.bridge.runtime;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import top.dzurl.task.bridge.device.type.DeviceType;
import top.dzurl.task.bridge.helper.ScriptEventHelper;
import top.dzurl.task.bridge.script.ScriptEvent;
import top.dzurl.task.bridge.script.ScriptRuntime;
import top.dzurl.task.bridge.script.SuperScript;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DeviceRunTimeManager {

    @Autowired
    private ScriptEventHelper scriptEventHelper;

    private Map<DeviceType, SuperDeviceRunTime> _cache = new ConcurrentHashMap<>();


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
        executeEvent(script, ScriptEvent.EventType.Create);
    }


    /**
     * 关闭环境
     */
    public void close(SuperScript script) {
        executeEvent(script, ScriptEvent.EventType.Close);
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
