package script.demo

import com.github.script.task.bridge.device.impl.NoDevice
import com.github.script.task.bridge.script.Environment
import com.github.script.task.bridge.script.Parameter
import com.github.script.task.bridge.script.ScriptEvent
import com.github.script.task.bridge.script.SuperScript
import com.github.script.task.bridge.service.RemoveDuplicateService
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired

class GetWeiBoUserScript extends SuperScript {

    @Autowired
    private RemoveDuplicateService removeDuplicateService;


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
                'cookie' : new Parameter(value: 'SINAGLOBAL=4295076059692.5376.1586332527804; login_sid_t=4574926056df9821576e726403230869; cross_origin_proto=SSL; _s_tentry=www.baidu.com; Apache=7706960892441.834.1615531942069; ULV=1615531942073:13:2:1:7706960892441.834.1615531942069:1614841585473; WBtopGlobal_register_version=2021031215; SSOLoginState=1615533281; un=18682630458; wvr=6; wb_view_log_2906191177=2560*14401; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhJ2TWaCdSW3kzQUE_eojmZ5JpX5KMhUgL.Foz4ehqp1K2pS0M2dJLoIEXLxK-L1KML1-eLxKML1-BLBK2LxKML1-2L1hBLxK-L1KqLBo-LxK-L1K5L1-zt; ALF=1647409705; SCF=Agyy7fZ5uwLCOE_SfvwDtyHWN5W-pBRELMkWd9hfLHsxbnOD8iprTbs0-vbyPeZebiiOAhPMOQNgOUzRRsYSN88.; SUB=_2A25NVDb7DeRhGeRH61QQ-S_NzDuIHXVuIC8zrDV8PUNbmtANLRSjkW9NTfMTS2IHWjVQH1WSJHEEwjtpOAOL5QJM; UOR=,,login.sina.com.cn; webim_unReadCount={"time":1615882155165,"dm_pub_total":0,"chat_group_client":0,"chat_group_notice":0,"allcountNum":38,"msgbox":0}'),
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
