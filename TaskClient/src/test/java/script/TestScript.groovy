package script


import top.dzurl.task.bridge.script.Environment
import top.dzurl.task.bridge.script.Parameter
import top.dzurl.task.bridge.script.SuperScript

class TestScript extends SuperScript {


    @Override
    Map<String, Parameter> parameters() {
        return [
                'phone': [
                        'remark': '平台登录的手机号码',
                        'value' : '15123241353'
                ] as Parameter
        ]
    }

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
    Object run() {
        return [
                'time': System.currentTimeMillis()
        ]
    }
}
