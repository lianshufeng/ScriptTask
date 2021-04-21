package com.github.script.task.client.core.job;

import com.github.script.task.bridge.conf.ScriptTaskConf;
import com.github.script.task.bridge.device.type.DeviceType;
import com.github.script.task.bridge.helper.CurrentStateHelper;
import com.github.script.task.bridge.helper.ScriptStoreHelper;
import com.github.script.task.bridge.model.JobModel;
import com.github.script.task.bridge.model.param.JobParam;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import com.github.script.task.bridge.service.JobService;
import com.github.script.task.bridge.service.ScriptService;
import com.github.script.task.bridge.util.JsonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class RemoteJobService {

    @Autowired
    private ScriptTaskConf scriptTaskConf;

    @Autowired
    private CurrentStateHelper currentStateHelper;

    @Autowired
    private JobService jobService;

    @Autowired
    private ScriptService scriptService;

    @Autowired
    private ScriptStoreHelper scriptStoreHelper;

    private ExecutorService executorService = null;

    //定时器
    private final static Timer timer = new Timer();


    @Autowired
    private void initThreadPool(ApplicationContext applicationContext) {
        //初始化线程池
        executorService = Executors.newFixedThreadPool(this.scriptTaskConf.getMaxRunTaskCount());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Optional.ofNullable(executorService).ifPresent((it) -> {
                it.shutdownNow();
            });
        }));

    }


    /**
     * 开始获取工作的
     *
     * @param applicationContext
     */
    @Autowired
    private void startGetJob(ApplicationContext applicationContext) {
        ScriptTaskConf.RemoteTask remoteTask = scriptTaskConf.getRemoteTask();
        if (remoteTask == null || remoteTask.isWork() == false) {
            return;
        }

        log.info("[执行] - [远程任务]");
        //定时器接任务
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    requestJob();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 5000, 5000);

        //进程销毁的时候自动结束定时器
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            timer.cancel();
        }));
    }


    @SneakyThrows
    private void requestJob() {
        //获取可执行的任务
        ResultContent resultContent = jobService.getJob(buildRequestJobParam());
        if (resultContent == null || resultContent.getState() != ResultState.Success) {
            return;
        }
        JobModel jobModel = JsonUtil.toObject(JsonUtil.toJson(resultContent.getContent()), JobModel.class);
        log.info("[脚本] - [执行] - [{}]", jobModel.getScriptName());

        //执行Job
        executorService.execute(() -> {
            this.execute(jobModel);
        });


    }


    /**
     * 构建请求Job的参数
     *
     * @return
     */
    private JobParam buildRequestJobParam() {
        final Set<String> deviceIds = this.currentStateHelper.getDeviceIds();
        final Set<DeviceType> powerTypes = new HashSet<>(this.currentStateHelper.getPowerType());
        return JobParam.builder().deviceIds(deviceIds).deviceTypes(powerTypes).build();
    }


    /**
     * 执行Job
     *
     * @param job
     */
    private void execute(JobModel job) {

        //获取脚本名
        String scriptName = job.getScriptName();

        //载入脚本代码
        String scriptCode = scriptStoreHelper.loadScript(scriptName);
        if (!StringUtils.hasText(scriptCode)) {
            log.error("脚本不存在 : {}", scriptName);
            return;
        }
        //设置工作id
        job.setJobId(job.getId());
        //执行脚本
        this.scriptService.executeScript(scriptCode, job);

    }


}
