package script.demo

import org.jsoup.Jsoup
import top.dzurl.task.bridge.device.impl.NoDevice
import top.dzurl.task.bridge.script.Environment
import top.dzurl.task.bridge.script.SuperScript

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
        def list = []

        def ret = Jsoup.connect('http://top.baidu.com').get();
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
