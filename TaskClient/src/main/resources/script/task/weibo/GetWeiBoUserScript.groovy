package script.task.weibo

import com.github.script.task.bridge.device.impl.NoDevice
import com.github.script.task.bridge.model.param.TaskParam
import com.github.script.task.bridge.result.ResultContent
import com.github.script.task.bridge.script.Environment
import com.github.script.task.bridge.script.Parameter
import com.github.script.task.bridge.script.ScriptEvent
import com.github.script.task.bridge.script.SuperScript
import com.github.script.task.bridge.script.action.net.HttpAction
import com.github.script.task.bridge.script.action.task.TaskAction
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

    private final static String host = "https://s.weibo.com/weibo"

    private final static String callScript = "AnalysisUserScript"


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
                'cookie' : new Parameter(value: 'SUB=_2A25NkJuiDeRhGeRH61QQ-S_NzDuIHXVu54pqrDV8PUNbmtAfLVLakW9NTfMTS4r9IKCKtLqL4uWelm3OgxsTnGuH; SSOLoginState=1620372466; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhJ2TWaCdSW3kzQUE_eojmZ5JpX5o275NHD95QE1K5ceK.peKMNWs4Dqcj_i--fiK.NiKLhi--NiKL2i-2pi--NiKLWiKnXi--fiK.ci-zfi--fiK.7iKLF; _s_tentry=-; ALF=1620977266; WBtopGlobal_register_version=2021050715; SINAGLOBAL=530312433674.3714.1620372432431; Apache=530312433674.3714.1620372432431; ULV=1620372432436:1:1:1:530312433674.3714.1620372432431:;'),
                'keyWord': new Parameter(value: 'rng',remark: '检索词'),
                'collectionNames':new Parameter(value: 'rng，进击的巨人',remark: '词库名称（多词库用逗号隔开）'),
                'year': new Parameter(value: '2020，2021',remark: '年份（多个用逗号隔开）'),
                'phone': new Parameter(value: '18682630458',remark: '登录手机号')
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
                    taskAction = action(TaskAction.class)
                    log.info('event : {}', 'run')
                },
                'onException': {
                    it ->
                        log.info('event : {} -> ', 'exception', it)
                }
        ] as ScriptEvent
    }

    TaskAction taskAction = null


    @Override
    Object run() {
        def ret = []
        def parameters = getRuntime().getParameters()
        def cookie = parameters['cookie']
        def keyWord = parameters['keyWord']
        def page = 1
        def url = host + '?q=' + keyWord
        def flag = true
        while(flag) {
            def document = connect(url, cookie, page)
            if (document.size() == 0){
                log.info("cookie失效，重新创建cookie")
                TaskParam param = new TaskParam()
                param.setScriptName("WeiBoSMSLogin")
                param.setParameters(['phone':getRuntime().getParameters().get('phone')])
                String jobId = taskAction.createTask(param,true)
                if (jobId != null){
                    taskAction.updateJobCreatTime(jobId,0)
                }
                flag = false
            }
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
                param.setScriptName(callScript)
                param.setParameters(['cookie':getRuntime().getParameters().get('cookie'),'host':it,'collectionNames':getRuntime().getParameters().get('collectionNames'),'sizePage':'30','year':getRuntime().getParameters().get('year')])
                taskAction.createTask(param,true)
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
