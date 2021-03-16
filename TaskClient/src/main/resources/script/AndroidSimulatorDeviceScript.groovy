package script

import io.appium.java_client.android.AndroidDriver
import org.springframework.beans.factory.annotation.Autowired
import com.github.script.task.bridge.device.impl.AndroidSimulatorDevice
import com.github.script.task.bridge.helper.MapHelper
import com.github.script.task.bridge.script.Environment
import com.github.script.task.bridge.script.ScriptEvent
import com.github.script.task.bridge.script.SuperScript
import com.github.script.task.bridge.script.action.device.SimulatorDeviceAction

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

        SimulatorDeviceAction simulatorAction = action(SimulatorDeviceAction.class);
        println simulatorAction.getMacAddress()

        return [
                'time': System.currentTimeMillis(),
        ]
    }
}
