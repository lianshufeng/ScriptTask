package script.demo

import com.github.script.task.bridge.device.impl.NoDevice
import com.github.script.task.bridge.model.param.TaskParam
import com.github.script.task.bridge.script.Environment
import com.github.script.task.bridge.script.Parameter
import com.github.script.task.bridge.script.ScriptEvent
import com.github.script.task.bridge.script.SuperScript
import com.github.script.task.bridge.service.JobService
import com.github.script.task.bridge.service.RemoveDuplicateService
import com.github.script.task.bridge.service.TaskService
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job

class GetWeiBoUserScript extends SuperScript {

    @Autowired
    private RemoveDuplicateService removeDuplicateService

    @Autowired
    private JobService jobService

    @Autowired
    private TaskService taskService


    @Override
    String name() {
        return 'GetWeiBoUserScript'
    }

    @Override
    String remark() {
        return '获取微博的用户'
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
                'cookie' : new Parameter(value: 'SINAGLOBAL=4295076059692.5376.1586332527804; un=18682630458; wvr=6; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhJ2TWaCdSW3kzQUE_eojmZ5JpX5KMhUgL.Foz4ehqp1K2pS0M2dJLoIEXLxK-L1KML1-eLxKML1-BLBK2LxKML1-2L1hBLxK-L1KqLBo-LxK-L1K5L1-zt; ALF=1647654735; SSOLoginState=1616118736; SCF=Agyy7fZ5uwLCOE_SfvwDtyHWN5W-pBRELMkWd9hfLHsx53uWq1GwT4LPi7kCVYDtwxHlF5tGTZaXVNiMXb8Bngw.; SUB=_2A25NUHOADeRhGeRH61QQ-S_NzDuIHXVuJOJIrDV8PUNbmtANLXTAkW9NTfMTS1P0Xx-gcAAN6WEk0GGVtOvxuNoz; _s_tentry=login.sina.com.cn; UOR=,,www.baidu.com; Apache=56067474428.02762.1616118739470; ULV=1616118739475:14:3:2:56067474428.02762.1616118739470:1615531942073; WBStorage=8daec78e6a891122|undefined'),
                'host' : new Parameter(value: 'https://s.weibo.com/weibo'),
                'keyWord': new Parameter(value: '美术生')
        ]
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
        def ret = []
        def parameters = getRuntime().getParameters()
        def cookie = parameters['cookie']
        def host = parameters['host']
        def keyWord = parameters['keyWord']
        def page = 1
        def url = host + '?q=' + keyWord
        def flag = true
        while(flag) {
            def document = connect(url, cookie, page)
            document.forEach((it) -> {
                def timeText = it.getElementsByClass('from').get(0).getElementsByTag('a').text()
                if (timeText.indexOf('分') > 0 || timeText.indexOf('秒') > 0) {
                    def userUrl = it.getElementsByClass('avator').get(0).getElementsByTag('a').attr('href')
                    def nickName = it.getElementsByClass('name').get(0).attr('nick-name')
                    //log.info('昵称：' + nickName + ',用户地址：' + userUrl + ',发送时间：' + timeText)
                    ret.push('https:' + userUrl)
                } else {
                    if (page != 1) {
                        flag = false
                    }
                }
            })
            page++
        }
        def result = removeDuplicateService.check(name(),ret)
        if (result != null && result['state'] == 'Success'){
            def content = result['content']
                //Todo 调用其他脚本
            TaskParam param = new TaskParam()
            param.setScriptName("")

            def taskResult = taskService.creatTask(param)


        }
        return [
                'time' : System.currentTimeMillis(),
                'title': result
        ]
    }

    def connect(def url , def cookie, def page ){
        url = url + '&page=' + page
        return Jsoup.connect(url).cookies(convertCookie(cookie)).get().getElementsByAttribute('mid')
    }

    HashMap<String, String> convertCookie(String cookie) {
        HashMap<String, String> cookiesMap = new HashMap<String, String>()
        String[] items = cookie.trim().split(';')
        for (String item:items) cookiesMap.put(item.split('=')[0], item.split('=')[1])
        return cookiesMap
    }
}
