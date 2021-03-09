import top.dzurl.task.bridge.device.impl.WebChromeDevice
import top.dzurl.task.bridge.script.Environment
import top.dzurl.task.bridge.script.Parameter
import top.dzurl.task.bridge.script.SuperScript

class TestScript1 extends SuperScript {


    @Override
    Map<String, Parameter> parameters() {
        return [
                'phone'    : [
                        'remark': '平台登录的手机号码',
                        'value' : '15123241353'
                ] as Parameter,
                'startTime': 0
        ]
    }

    @Override
    String name() {
        return "测试脚本1"
    }

    @Override
    String remark() {
        '我的快乐至上11'
    }

    @Override
    Environment environment() {
        return [
                'device': [
                ] as WebChromeDevice
        ] as Environment
    }

    @Override
    Object run() {
        long startTime = getRuntime().getParameters().get('startTime');


        return null
    }
}
