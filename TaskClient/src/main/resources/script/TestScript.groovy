package script


import top.dzurl.task.bridge.script.Environment
import top.dzurl.task.bridge.script.Parameter
import top.dzurl.task.bridge.script.ScriptEvent
import top.dzurl.task.bridge.script.SuperScript

class TestScript extends SuperScript {


    @Override
    String name() {
        return "TestScript"
    }

    @Override
    String remark() {
        '我的快乐至上11'
    }

    @Override
    Environment environment() {
        return [

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
                        log.info('event : {} -> ', 'exception', it)
                }
        ] as ScriptEvent
    }

    @Override
    Object run() {
        return [
                'time': System.currentTimeMillis()
        ]
    }
}
