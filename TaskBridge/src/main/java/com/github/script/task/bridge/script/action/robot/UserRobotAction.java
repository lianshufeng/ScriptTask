package com.github.script.task.bridge.script.action.robot;

import com.github.script.task.bridge.model.userrobot.parm.RobotInterfacePutParm;
import com.github.script.task.bridge.model.userrobot.robot.RobotInput;
import com.github.script.task.bridge.model.userrobot.robot.RobotInterface;
import com.github.script.task.bridge.model.userrobot.type.RobotInterfaceType;
import com.github.script.task.bridge.model.userrobot.user.UserInterface;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import com.github.script.task.bridge.script.SuperScriptAction;
import com.github.script.task.bridge.service.UserRobotInterfaceService;
import com.github.script.task.bridge.util.JsonUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 人机交互
 */
@Slf4j
public class UserRobotAction extends SuperScriptAction {

    @Autowired
    private UserRobotInterfaceService userRobotInterfaceService;

    //线程池
    private ScheduledExecutorService threadPool = null;

    @Autowired
    private void init(ApplicationContext applicationContext) {
        threadPool = getScript().getRuntime().getThreadPool();
    }

    //请求用户输入的频率
    private final static long RequestSleepTime = 3000;

    //请求调度器
    private ScheduledFuture requestScheduledFuture = null;
    private CountDownLatch requestCountDownLatch;


    /**
     * 等待人机交互，用户输入
     */
    @SneakyThrows
    public UserInput waitUserInput(UserRobotInterface userRobotInterface) {

        //取出value的类型
        RobotInterfaceType type = null;
        RobotInterface value = userRobotInterface.getValue();
        if (value == null) {
            type = RobotInterfaceType.Input;
            value = new RobotInput();
        } else {
            //遍历并取出类型
            for (RobotInterfaceType typeItem : RobotInterfaceType.values()) {
                if (value.getClass() == typeItem.getRobotClass()) {
                    type = typeItem;
                    break;
                }
            }
        }

        //构建创建人机交互任务的模型
        RobotInterfacePutParm robotInterfacePutParm = new RobotInterfacePutParm();
        robotInterfacePutParm.setTips(userRobotInterface.getTips());
        robotInterfacePutParm.setTimeOut(userRobotInterface.getTimeOut());
        robotInterfacePutParm.setTaskId(getScript().getRuntime().getTaskId());
        robotInterfacePutParm.setType(type);
        robotInterfacePutParm.setValue(value);

        //添加人机交互
        ResultContent<String> resultContent = userRobotInterfaceService.addUserRobotInterface(robotInterfacePutParm);

        final UserInput userInput = new UserInput();
        userInput.type = type;

        //人机交互的任务创建失败
        if (resultContent.getState() != ResultState.Success) {
            userInput.state = resultContent.getState();
            return userInput;
        }

        //人机交互的id
        final String id = resultContent.getContent();
        log.info("[人机交互] - [{}]", id);

        requestCountDownLatch = new CountDownLatch(1);
        //进行请求并获取用户输入完成的人机交互
        threadPool.schedule(() -> {
            requestUserInput(userInput, id);
        }, RequestSleepTime, TimeUnit.MILLISECONDS);
        requestCountDownLatch.await();

        //删除人机交互
        this.userRobotInterfaceService.remove(id);

        return userInput;
    }


    /**
     * 请求人机交互获取用户输入
     */
    private void requestUserInput(final UserInput userInput, String id) {
        ResultContent resultContent = this.userRobotInterfaceService.getUserInput(id);

        if (validateUserInput(ResultState.Success, resultContent, userInput)) {
            return;
        }

        if (validateUserInput(ResultState.UserRobotNotExists, resultContent, userInput)) {
            return;
        }


        //延迟请求
        threadPool.schedule(() -> {
            requestUserInput(userInput, id);
        }, RequestSleepTime, TimeUnit.MILLISECONDS);
    }


    private boolean validateUserInput(ResultState state, ResultContent resultContent, final UserInput userInput) {
        if (resultContent != null && resultContent.getState() != null && resultContent.getState() == state) {
            userInput.state = resultContent.getState();
            userInput.content = resultContent.getContent();
            requestCountDownLatch.countDown();
            return true;
        }
        return false;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserRobotInterface {

        //提示
        private String tips;


        //人机交互的参数
        private RobotInterface value;


        //过期时间
        private Long timeOut;

    }


    /**
     * 获取用户输入对象
     */
    public static class UserInput {

        @Getter
        protected RobotInterfaceType type;

        //获取的数据
        @Getter
        protected Object content;


        //状态
        @Getter
        protected ResultState state;

        /**
         * 取出用户输入
         *
         * @return
         */
        @SneakyThrows
        public UserInterface getUserInterface() {
            return JsonUtil.toObject(JsonUtil.toJson(content), type.getUserClass());
        }


    }


}
