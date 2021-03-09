package top.dzurl.task.client.core.runtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import top.dzurl.task.bridge.device.type.DeviceType;
import top.dzurl.task.bridge.script.ScriptRuntime;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DeviceRunTimeManager {

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
    public void open(ScriptRuntime runtime) {
        SuperDeviceRunTime runTime = getRunTime(runtime);
        runTime.open(runtime);
    }


    /**
     * 关闭环境
     */
    public void close(ScriptRuntime runtime) {
        SuperDeviceRunTime runTime = getRunTime(runtime);
        runTime.close(runtime);
    }


    /**
     * 发布事件
     *
     * @param runTime
     */
    private void event(SuperDeviceRunTime runTime) {

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
