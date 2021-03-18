package com.github.script.task.client.core.job;

import com.github.script.task.bridge.conf.ScriptTaskConf;
import com.github.script.task.bridge.device.type.DeviceType;
import com.github.script.task.bridge.helper.CurrentStateHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Component
public class RemoteJobService {

    @Autowired
    private ScriptTaskConf scriptTaskConf;

    //定时器
    private Timer timer = new Timer();


    @Autowired
    private CurrentStateHelper currentStateHelper;


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

        log.info("[允许] - [执行任务]");
        //定时器接任务
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                requestJob();
            }
        }, 5000, 5000);

        //进程销毁的时候自动结束定时器
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            timer.cancel();
        }));
    }


    @SneakyThrows
    private void requestJob() {
        build();
    }

    private void build() {
        final Set<String> deviceIds = currentStateHelper.getDeviceIds();
        final Set<DeviceType> powerTypes = new HashSet<>(currentStateHelper.getPowerType());

    }


}
