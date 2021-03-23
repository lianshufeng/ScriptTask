package script

import com.github.script.task.bridge.device.impl.AndroidSimulatorDevice
import com.github.script.task.bridge.helper.MapHelper
import com.github.script.task.bridge.script.Environment
import com.github.script.task.bridge.script.action.async.AsyncAction
import com.github.script.task.bridge.script.ScriptEvent
import com.github.script.task.bridge.script.SuperScript
import com.github.script.task.bridge.script.action.bind.DeviceBindAction
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
        action(DeviceBindAction.class).bind()
        def asyncAction = action(AsyncAction.class)


//
//        scriptAsync.await();


        return [
                'time': System.currentTimeMillis(),
        ]
    }
}
