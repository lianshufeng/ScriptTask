package script.simple

import com.github.script.task.bridge.device.impl.AndroidSimulatorDevice
import com.github.script.task.bridge.helper.MapHelper
import com.github.script.task.bridge.model.userrobot.robot.RobotInput
import com.github.script.task.bridge.model.userrobot.user.UserInput
import com.github.script.task.bridge.model.userrobot.user.UserInterface
import com.github.script.task.bridge.result.ResultState
import com.github.script.task.bridge.script.Environment
import com.github.script.task.bridge.script.ScriptEvent
import com.github.script.task.bridge.script.SuperScript
import com.github.script.task.bridge.script.action.async.AsyncAction
import com.github.script.task.bridge.script.action.bind.DeviceBindAction
import com.github.script.task.bridge.script.action.robot.UserRobotAction
import org.springframework.beans.factory.annotation.Autowired

class AndroidSimulatorDeviceScript extends SuperScript {


    @Override
    String name() {
        return "AndroidSimulatorDeviceScript"
    }

    @Override
    String remark() {
        '我的快乐至上11'
    }

    @Override
    Environment environment() {
        return [
                'device': [
                        "resolution": "1080,1920,260"
                ] as AndroidSimulatorDevice
        ] as Environment
    }

    @Override
    ScriptEvent event() {
        return [
                'onCreate'   : {
                    log.info('event : {}', 'create')
                },
                'onClose'    : {
                    log.info('event : {}', 'close')
                },
                'onRun'      : {
                    log.info('event : {}', 'run')
                },
                'onException': {
                    it ->
                        log.info('event : {} -> {}', 'exception ', it)
                }
        ] as ScriptEvent
    }


    @Autowired
    private MapHelper mapHelper;

    @Override
    Object run() {
        //设备绑定
//        action(DeviceBindAction.class).bind()

        //异步方法
//        def asyncAction = action(AsyncAction.class)


        //等待用户输入
        UserRobotAction.UserInput userInput = action(UserRobotAction.class).waitUserInput([
                'tips'   : '请输入收到的短信',
                'value'  : [] as RobotInput,
                'timeOut': 6000
        ] as UserRobotAction.UserRobotInterface)
        if (userInput.getState() == ResultState.Success) {
            UserInput userRobot = userInput.getUserInterface();
            println "用户输入 : " + userRobot.getText()


        }


        return [
                'time': System.currentTimeMillis(),
        ]
    }
}
