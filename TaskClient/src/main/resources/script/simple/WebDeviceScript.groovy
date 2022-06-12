package script.simple


import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import com.github.script.task.bridge.device.impl.WebDevice
import com.github.script.task.bridge.script.Environment
import com.github.script.task.bridge.script.ScriptEvent
import com.github.script.task.bridge.script.SuperScript

class WebDeviceScript extends SuperScript {


    @Override
    String name() {
        return "WebDeviceScript"
    }

    @Override
    String remark() {
        '我的快乐至上11'
    }

    @Override
    Environment environment() {
        return [
                'device': [
                        'headless': false //无头模式
                ] as WebDevice
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

        ChromeDriver driver = runtime.getDriver()

        def ret = [:]

        //取出热搜的第一条连接
        driver.get('https://top.baidu.com/board?tab=realtime')

        println driver.findElement(By.className("container_2VTvm")).getText()

//        def a = driver.findElement(By.id('pl_top_realtimehot'))
//                .findElement(By.tagName('table'))
//                .findElement(By.tagName('tbody'))
//                .findElements(By.tagName('tr'))
//                .get(1).findElement(By.tagName('a'))
//        def title = a.getText()
//        def url = a.getAttribute('href')
//        log.info('主题 : {}', title)
////
////        //打开这个主题
//        driver.get(url)
////
//        //取出对这个主题的发言的用户
//        def element = driver.findElement(By.id('pl_feedlist_index')).findElements(By.className('info'));
//        element.forEach((it) -> {
//            try {
//                def nameA = it.findElement(By.className('name'))
//                ret.put(nameA.getText(), nameA.getAttribute('href'))
//                log.info('[ {} ] -> {}', nameA.getText(), nameA.getAttribute('href'))
//            } catch (Exception e) {
//            }
//        })


        return [
                'time' : System.currentTimeMillis(),
                'title': driver.getTitle(),
                'body' : driver.findElement(By.tagName("body"))
        ]
    }
}
