package com.github.script.task.bridge.script;

import com.github.script.task.bridge.helper.SpringBeanHelper;
import com.github.script.task.bridge.model.ScriptRunTimeModel;
import com.github.script.task.bridge.runtime.DeviceRunTimeManager;
import com.github.script.task.bridge.util.BeanUtil;
import com.github.script.task.bridge.util.ScriptUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Executors;

/**
 * 脚本运行环境工厂
 */
@Slf4j
@Component
public class ScriptFactory {

    @Autowired
    private SpringBeanHelper springBeanHelper;

    @Autowired
    private DeviceRunTimeManager deviceRunTimeManager;


    /**
     * 转换脚本文本为脚本对象，并注入各种环境
     */
    public SuperScript parse(String scriptContent, final ScriptRunTimeModel runTimeModel) {
        SuperScript script = ScriptUtil.parse(scriptContent);

        //运行环境
        runTime(script, runTimeModel);

        //日志功能
        log(script);

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
        Object ret = executeScript(script);
        this.deviceRunTimeManager.close(script);
        return (T) ret;
    }


    /**
     * 执行脚本
     */
    @SneakyThrows
    private Object executeScript(SuperScript script) {
        final ScriptTimout scriptTimout = new ScriptTimout(script);
        this.springBeanHelper.injection(scriptTimout);
        return scriptTimout.execute();
    }


    /**
     * 释放脚本
     *
     * @param script
     */
    public void release(SuperScript script) {
        shutdownThreadPool(script);
    }

    /**
     * 关闭线程池
     */
    private void shutdownThreadPool(SuperScript script) {
        //关闭所有线程池
        Optional.ofNullable(script.getRuntime()).ifPresent((it) -> {
            try {
                //关闭线程池
                if (it.getThreadPool() != null) {
                    it.getThreadPool().shutdownNow();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * 设置脚本的运行环境
     *
     * @param script
     */
    private void runTime(SuperScript script, final ScriptRunTimeModel runTimeModel) {
        //运行环境
        final ScriptRuntime scriptRuntime = ScriptRuntime.builder().script(script).build();
        script.runtime = scriptRuntime;
        BeanUtils.copyProperties(runTimeModel, scriptRuntime, "parameters", "environment");

        //脚本事件
        scriptRuntime.setScriptEvent(script.event());

        //合并运行的脚本环境
        final Environment runTimeEnvironment = mergeEnvironment(script.environment(), runTimeModel.getEnvironment());
        scriptRuntime.setEnvironment(runTimeEnvironment);

        //合并运行的脚本参数
        final Map<String, Object> scriptParameters = new HashMap<>();
        script.parameters().entrySet().forEach((it) -> {
            scriptParameters.put(it.getKey(), it.getValue().getValue());
        });
        final Map<String, Object> runTimeParameters = mergeParameter(scriptParameters, runTimeModel.getParameters());
        scriptRuntime.setParameters(runTimeParameters);

        //线程池
        scriptRuntime.setThreadPool(Executors.newScheduledThreadPool(runTimeEnvironment.getThreadPoolCount()));


    }


    /**
     * 设置日志功能
     *
     * @param script
     */
    private void log(SuperScript script) {
        script.log = ScriptLog.builder().script(script).build();
        springBeanHelper.injection(script.log);
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
