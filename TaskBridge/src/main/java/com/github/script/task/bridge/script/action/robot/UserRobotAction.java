package com.github.script.task.bridge.script.action.robot;

import com.github.script.task.bridge.model.userrobot.parm.RobotInterfaceParm;
import com.github.script.task.bridge.model.userrobot.type.RobotInterfaceType;
import com.github.script.task.bridge.script.SuperScriptAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ScheduledExecutorService;

/**
 * 人机交互
 */
@Slf4j
public class UserRobotAction extends SuperScriptAction {

    //线程池
    private ScheduledExecutorService threadPool = null;

    @Autowired
    private void init(ApplicationContext applicationContext) {
        //取出线程池
        threadPool = getScript().getRuntime().getThreadPool();
    }

    /**
     * 等待用户输入
     */
    public UserInput waitUserInput(RobotInterfaceParm robotInterfaceParm) {

        //人机交互超时时间
        Long timeOut = robotInterfaceParm.getTimeOut();





        return null;
    }


    /**
     * 获取用户输入对象
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInput {

        private String value;


    }


}
