package com.github.script.task.client.core.job;

import com.github.script.task.bridge.conf.ScriptTaskConf;
import com.github.script.task.bridge.device.type.DeviceType;
import com.github.script.task.bridge.model.ClientModel;
import com.github.script.task.bridge.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Component
public class ClientHeartbeatService {

    @Autowired
    private ScriptTaskConf scriptTaskConf;

    @Autowired
    private ClientService clientService;

    //定时器
    private Timer timer;

    private ClientModel clientModel;

    //心跳时间
    private static final long HeartbeatTime = 10000;


    @Autowired
    private void init(ApplicationContext applicationContext) {
        this.timer = new Timer();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            timer.cancel();
        }));

        //创建客户端
        buildClient();

        //启动则触发心跳
        heartbeat();
    }

    private void buildClient() {
        Properties props = System.getProperties();
        clientModel = new ClientModel();
        clientModel.setUuid(UUID.randomUUID().toString());
        clientModel.setDeviceTypes(scriptTaskConf.getRemoteTask().getPowerType().toArray(new DeviceType[0]));
        clientModel.setOsName(props.getProperty("os.name"));
        clientModel.setOsVersion(props.getProperty("os.version"));
    }

    private void heartbeat() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    task();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                heartbeat();
            }
        }, HeartbeatTime);
    }


    /**
     * 心跳任务
     */
    private void task() {
        this.clientService.heartbeat(this.clientModel);
    }
}
