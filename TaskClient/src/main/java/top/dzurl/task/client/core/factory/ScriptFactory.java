package top.dzurl.task.client.core.factory;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.dzurl.task.bridge.helper.ScriptHelper;
import top.dzurl.task.bridge.helper.SpringBeanHelper;
import top.dzurl.task.bridge.script.Environment;
import top.dzurl.task.bridge.script.ScriptAsync;
import top.dzurl.task.bridge.script.ScriptRuntime;
import top.dzurl.task.bridge.script.SuperScript;
import top.dzurl.task.bridge.util.BeanUtil;
import top.dzurl.task.client.core.runtime.DeviceRunTimeManager;

import java.util.*;
import java.util.concurrent.Executors;

/**
 * 脚本运行环境工厂
 */
@Component
public class ScriptFactory {

    @Autowired
    private ScriptHelper scriptHelper;

    @Autowired
    private SpringBeanHelper springBeanHelper;

    @Autowired
    private DeviceRunTimeManager deviceRunTimeManager;


    /**
     * 转换脚本文本为脚本对象，并注入各种环境
     */
    public SuperScript parse(String scriptContent, final Environment environment, final Map<String, Object> parameters) {
        SuperScript script = this.scriptHelper.parse(scriptContent);

        //运行环境
        runTime(script, environment, parameters);

        //异步功能
        async(script);

        //注入spring容器的依赖对象
        this.springBeanHelper.injection(script);

        return script;
    }


    /**
     * 运行脚本
     *
     * @param script
     */
    public <T> T run(SuperScript script) {
        this.deviceRunTimeManager.create(script);
        Object ret = script.execute();
        this.deviceRunTimeManager.close(script);
        return (T) ret;
    }


    /**
     * 设置脚本的运行环境
     *
     * @param script
     */
    private void runTime(SuperScript script, final Environment environment, final Map<String, Object> parameters) {
        //运行环境
        final ScriptRuntime scriptRuntime = new ScriptRuntime();
        script.setRuntime(scriptRuntime);

        //合并运行的脚本环境
        final Environment runTimeEnvironment = mergeEnvironment(script.environment(), environment);
        scriptRuntime.setEnvironment(runTimeEnvironment);

        //合并运行的脚本参数
        final Map<String, Object> scriptParameters = new HashMap<>();
        script.parameters().entrySet().forEach((it) -> {
            scriptParameters.put(it.getKey(), it.getValue().getValue());
        });
        final Map<String, Object> runTimeParameters = mergeParameter(scriptParameters, parameters);
        scriptRuntime.setParameters(runTimeParameters);

        //线程池
        scriptRuntime.setThreadPool(Executors.newScheduledThreadPool(runTimeEnvironment.getThreadPoolCount()));


    }


    /**
     * 设置异步功能
     *
     * @param script
     */
    private void async(SuperScript script) {
        script.setAsync(ScriptAsync.builder().script(script).build());
        this.springBeanHelper.injection(script.getAsync());
    }


    /**
     * 合并用户与脚本的参数
     *
     * @return
     */
    private Map<String, Object> mergeParameter(Map<String, Object>... sources) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (sources != null && sources.length > 0) {
            Arrays.stream(sources).forEach((source) -> {
                if (source != null) {
                    ret.putAll(source);
                }
            });
        }
        return ret;
    }

    /**
     * 合并环境
     *
     * @param target
     * @param source
     * @return
     */
    private Environment mergeEnvironment(Environment target, Environment source) {
        Optional.ofNullable(source).ifPresent((it) -> {
            BeanUtils.copyProperties(it, target, new HashSet<>() {{
                addAll(BeanUtil.getNullPropertyNames(it));
                add("device");
            }}.toArray(new String[0]));

            //Device
            Optional.ofNullable(it.getDevice()).ifPresent((device) -> {
                //设备类型不存在则完全使用用户传递的设备信息
                if (target.getDevice() == null) {
                    target.setDevice(device);
                } else {
                    BeanUtils.copyProperties(device, target.getDevice(), new HashSet<>() {{
                        addAll(BeanUtil.getNullPropertyNames(device));
                    }}.toArray(new String[0]));
                }
            });
        });


        return target;
    }

}
