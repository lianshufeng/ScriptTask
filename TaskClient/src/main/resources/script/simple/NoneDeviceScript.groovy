package script.simple

import com.github.script.task.bridge.script.Parameter
import com.github.script.task.bridge.script.action.net.HttpAction
import org.jsoup.Jsoup
import com.github.script.task.bridge.device.impl.NoDevice
import com.github.script.task.bridge.script.Environment
import com.github.script.task.bridge.script.SuperScript

class NoneDeviceScript extends SuperScript {


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
                'device': new NoDevice()
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
    Object run() {
        def parameters = getRuntime().getParameters();

        HttpAction httpAction = action(HttpAction.class)
        httpAction.setProxy(parameters.get('proxy'));

        //查询当前的ip
//        println httpAction.get("https://2021.ip138.com").parse().html().getElementsByTag("title").text()

        def list = []
        def ret = httpAction.get('https://top.baidu.com', [:], "GBK").parse("GBK").html()
        ret.getElementById('hot-list').getElementsByTag('li').forEach((it) -> {
            def title = it.getElementsByTag('a').get(0).attr('title')
            list.push(title);
        })
        return [
                'time' : System.currentTimeMillis(),
                'title': list
        ]
    }
}
