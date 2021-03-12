package script

import io.appium.java_client.android.AndroidDriver
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import top.dzurl.task.bridge.device.impl.AndroidSimulatorDevice
import top.dzurl.task.bridge.script.Environment
import top.dzurl.task.bridge.script.ScriptEvent
import top.dzurl.task.bridge.script.SuperScript

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

    @Override
    Object run() {
        AndroidDriver driver = runtime.getDriver();

        return [
                'time': System.currentTimeMillis(),
        ]
    }
}
