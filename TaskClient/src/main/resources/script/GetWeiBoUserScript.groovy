package script.demo

import com.github.script.task.bridge.device.impl.NoDevice
import com.github.script.task.bridge.model.param.TaskParam
import com.github.script.task.bridge.result.ResultContent
import com.github.script.task.bridge.script.Environment
import com.github.script.task.bridge.script.Parameter
import com.github.script.task.bridge.script.ScriptEvent
import com.github.script.task.bridge.script.SuperScript
import com.github.script.task.bridge.service.DataDuplicateService
import com.github.script.task.bridge.service.JobService
import com.github.script.task.bridge.service.TaskService
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired

class GetWeiBoUserScript extends SuperScript {

    @Autowired
    private DataDuplicateService dataDuplicateService

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
                'cookie' : new Parameter(value: 'SINAGLOBAL=4295076059692.5376.1586332527804; SSOLoginState=1616118736; _s_tentry=login.sina.com.cn; UOR=,,www.baidu.com; Apache=56067474428.02762.1616118739470; ULV=1616118739475:14:3:2:56067474428.02762.1616118739470:1615531942073; WBtopGlobal_register_version=2021032310; un=18682630458; wb_view_log_2906191177=2560*14401; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhJ2TWaCdSW3kzQUE_eojmZ5JpX5KMhUgL.Foz4ehqp1K2pS0M2dJLoIEXLxK-L1KML1-eLxKML1-BLBK2LxKML1-2L1hBLxK-L1KqLBo-LxK-L1K5L1-zt; ALF=1648087629; SCF=Agyy7fZ5uwLCOE_SfvwDtyHWN5W-pBRELMkWd9hfLHsxB1B9U0yCTPWGzMPHBJiH_e8geuOmrDaAgiKwCLhOgnA.; SUB=_2A25NXu6eDeRhGeRH61QQ-S_NzDuIHXVuKkdWrDV8PUNbmtANLUbmkW9NTfMTSyi2kPsU4mS_pnVBW-Z7o1Sg6za8; webim_unReadCount={"time":1616551637943,"dm_pub_total":0,"chat_group_client":0,"chat_group_notice":0,"allcountNum":39,"msgbox":0}'),
                'host' : new Parameter(value: 'https://s.weibo.com/weibo'),
                'keyWord': new Parameter(value: '进击的巨人'),
                'isDel' : new Parameter(value: true),
                'callScript' : new Parameter(value: 'AnalysisUserScript'),
                'collectionNames':new Parameter(value: ['测试'])
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
        def isDel = parameters['true']
        def page = 1
        def url = host + '?q=' + keyWord
        def flag = true
        while(flag) {
            def document = connect(url, cookie, page)
            document.forEach((it) -> {
                def timeText = it.getElementsByClass('from').get(0).getElementsByTag('a').text()
                if (timeText.indexOf('分') > 0 || timeText.indexOf('秒') > 0) {
                    def userUrl = it.getElementsByClass('avator').get(0).getElementsByTag('a').attr('href')
                    ret.push('https:' + userUrl + "&is_ori=1")
                } else {
                    if (page != 1) {
                        flag = false
                    }
                }
            })
            page++
            createAnalysisUserJob(name(),ret)
            ret.clear()
            Thread.sleep(1000)
        }
        if (isDel){
            taskService.del(getRuntime().getTaskId())
        }
        return [
                'time' : System.currentTimeMillis()
        ]
    }

    def createAnalysisUserJob(def name,def ret){
        ResultContent result = dataDuplicateService.check(name,ret)
        if (result != null && result.getState().name() == 'Success'){
            List<String> content = result.getContent()
            content.forEach((it)->{
                TaskParam param = new TaskParam()
                param.setScriptName(getRuntime().getParameters().get('callScript'))
                param.setParameters(['cookie':getRuntime().getParameters().get('cookie'),'host':it,'collectionNames':getRuntime().getParameters().get('collectionNames'),'sizePage':50])
                def taskResult = taskService.creatTask(param)
                if (taskResult.getState().name() == 'Success'){
                    jobService.createJobByTaskId(taskResult['content']['id'])
                }
            })
        }
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
