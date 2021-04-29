package script.simple

import com.github.script.task.bridge.device.impl.NoDevice
import com.github.script.task.bridge.script.Environment
import com.github.script.task.bridge.script.Parameter
import com.github.script.task.bridge.script.ScriptEvent
import com.github.script.task.bridge.script.SuperScript
import com.github.script.task.bridge.script.action.net.HttpAction

class NoneDeviceScript extends SuperScript {


    // 不能用 private  ， 否则多个方法中无法访问
    HttpAction httpAction = null;

    @Override
    String name() {
        return "NoneDeviceScript"
    }

    @Override
    String remark() {
        '我的快乐至上11'
    }

    @Override
    Environment environment() {
        return [
                'device' : new NoDevice(),
                'timeout': 1000
        ] as Environment
    }

    @Override
    Map<String, Parameter> parameters() {
        return [
                "proxy": [
                        remark: '是否使用代理',
                        value : true
                ] as Parameter
        ]
    }

    @Override
    ScriptEvent event() {
        return [
                'onRun': {
                    log.info('event : {}', 'run')
                    def parameters = getRuntime().getParameters();
                    httpAction = action(HttpAction.class);
                    httpAction.setProxy(parameters.get('proxy'));
                }
        ] as ScriptEvent
    }


    @Override
    Object run() {


//        UserRobotAction.UserInput userInput1 = action(UserRobotAction.class).waitUserInput([
//                'tips'   : '请输入您收到的短信验证码,当前手机号码:151232*****',
//                'value'  : [] as RobotInput,
//                'timeOut': 60000 * 5
//        ] as UserRobotAction.UserRobotInterface)
//        println userInput1.getUserInterface()
//
//
//        UserRobotAction.UserInput userInput2 = action(UserRobotAction.class).waitUserInput([
//                'tips'   : '请输入识别的验证码',
//                'value'  : [
//                        'picture': 'data:image/png;base64,' + Base64.getEncoder().encodeToString(new HttpClient().get('https://login.sina.com.cn/cgi/pin.php?p=tc-0710d9dfb55a46ea6db97634ed77ec5e9929').getBin())
//                ] as RobotOcr,
//                'timeOut': 60000 * 5
//        ] as UserRobotAction.UserRobotInterface)
//        println userInput2.getUserInterface()


//        UserRobotAction.UserInput userInput3 = action(UserRobotAction.class).waitUserInput([
//                'tips'   : '请依次点击   "招" "河" "丽" ',
//                'value'  : [
//                        'items': [
//                                [
//                                        'name'   : 'test1',
//                                        'picture': 'data:image/png;base64,' + Base64.getEncoder().encodeToString(new HttpClient().get('https://necaptcha.nosdn.127.net/ce52885c5b144667a223933ec1fd12b6@2x.jpg').getBin())
//                                ] as RobotTap.Item,
//                                [
//                                        'name'   : 'test2',
//                                        'picture': 'data:image/png;base64,' + Base64.getEncoder().encodeToString(new HttpClient().get('https://necaptcha.nosdn.127.net/25172193a0b54d1eb04f04df78c01cfb@2x.jpg').getBin())
//                                ] as RobotTap.Item
//                        ]
//                ] as RobotTap,
//                'timeOut': 60000 * 50
//        ] as UserRobotAction.UserRobotInterface)
//        println userInput3.getUserInterface()


        //查询当前的ip
//        println httpAction.get("https://2021.ip138.com").parse().html().getElementsByTag("title").text()

        def list = []
        def ret = httpAction.get('https://top.baidu.com', [:], "GBK").parse("GBK").html()
        ret.getElementById('hot-list').getElementsByTag('li').forEach((it) -> {
            def title = it.getElementsByTag('a').get(0).attr('title')
            list.push(title);
        })
        log.info("list : {}", list)
        return [
                'time' : System.currentTimeMillis(),
                'title': list
        ]
    }
}
