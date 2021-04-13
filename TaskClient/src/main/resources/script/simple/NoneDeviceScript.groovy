package script.simple

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
    Object run() {
        HttpAction httpAction = action(HttpAction.class)

        def list = []

        def ret = httpAction.get('http://top.baidu.com').parse("GBK").html()
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
