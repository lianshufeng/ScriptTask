package script

import io.appium.java_client.android.AndroidDriver
import org.springframework.beans.factory.annotation.Autowired
import top.dzurl.task.bridge.device.impl.AndroidSimulatorDevice
import top.dzurl.task.bridge.helper.MapHelper
import top.dzurl.task.bridge.script.Environment
import top.dzurl.task.bridge.script.ScriptEvent
import top.dzurl.task.bridge.script.SuperScript
import top.dzurl.task.bridge.script.action.SimulatorAction

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
        AndroidDriver driver = runtime.getDriver()

        SimulatorAction simulatorAction = action(SimulatorAction.class);
        println simulatorAction.getMacAddress()

        return [
                'time': System.currentTimeMillis(),
        ]
    }
}
