package script

import com.github.script.task.bridge.device.impl.NoDevice
import com.github.script.task.bridge.script.Environment
import com.github.script.task.bridge.script.Parameter
import com.github.script.task.bridge.script.ScriptEvent
import com.github.script.task.bridge.script.SuperScript
import com.github.script.task.bridge.script.action.async.AsyncAction
import com.github.script.task.bridge.script.action.net.HttpAction
import com.github.script.task.bridge.script.action.word.MatchWordAction
import com.github.script.task.bridge.service.TaskService
import com.github.script.task.bridge.util.JsonUtil
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired

import java.util.stream.Collectors

class AnalysisUserScript extends SuperScript {

    private final static String FMView = "FM.view";

    @Autowired
    private TaskService taskService

    @Override
    String name() {
        return 'AnalysisUserScript'
    }

    @Override
    String remark() {
        return '解析微博用户'
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
                'cookie'  : new Parameter(value: 'SINAGLOBAL=4295076059692.5376.1586332527804; SSOLoginState=1616118736; _s_tentry=login.sina.com.cn; UOR=,,www.baidu.com; Apache=56067474428.02762.1616118739470; ULV=1616118739475:14:3:2:56067474428.02762.1616118739470:1615531942073; WBtopGlobal_register_version=2021032310; un=18682630458; wb_view_log_2906191177=2560*14401; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhJ2TWaCdSW3kzQUE_eojmZ5JpX5KMhUgL.Foz4ehqp1K2pS0M2dJLoIEXLxK-L1KML1-eLxKML1-BLBK2LxKML1-2L1hBLxK-L1KqLBo-LxK-L1K5L1-zt; ALF=1648087629; SCF=Agyy7fZ5uwLCOE_SfvwDtyHWN5W-pBRELMkWd9hfLHsxB1B9U0yCTPWGzMPHBJiH_e8geuOmrDaAgiKwCLhOgnA.; SUB=_2A25NXu6eDeRhGeRH61QQ-S_NzDuIHXVuKkdWrDV8PUNbmtANLUbmkW9NTfMTSyi2kPsU4mS_pnVBW-Z7o1Sg6za8; webim_unReadCount={"time":1616551637943,"dm_pub_total":0,"chat_group_client":0,"chat_group_notice":0,"allcountNum":39,"msgbox":0}'),
                'host'    : new Parameter(value: 'https://weibo.com/u/5680043727?profile_ftype=1&is_ori=1'),
                'isDel'   : new Parameter(value: true),
                'year'    : new Parameter(value: ['2020', '2021']),
                'sizePage': new Parameter(value: 1),
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


    void analysisWebContent(Set result, String host, String cookie, int page) {
        def info = new HashSet()
        def document = connect(host, cookie, page)
        def ret = document.getElementsByTag('script')
        ret.stream().map((it) -> {
            return it.html();
        }).filter((it) -> {
            return it.indexOf(FMView) == 0 && it.indexOf('WB_text W_f14') > -1 && it.indexOf('WB_from S_txt2') > -1
        }).map((it) -> {
            return it.substring(FMView.length() + 1, it.length() - 1)
        }).map((it) -> {
            return JsonUtil.toObject(it, Object.class)
        }).map((it) -> {
            return Jsoup.parse(it['html']).getElementsByClass('WB_detail')
        }).map((it) -> {
            return it.stream().map((element) -> {
                return [
                        'text': element.getElementsByClass('WB_text W_f14').get(0).text(),
                        'time': element.getElementsByClass('WB_from S_txt2').get(0).text()
                ]
            }).collect(Collectors.toSet())
        }).forEach((it) -> {
            info.addAll(it)
        })
        result.addAll(info)


        //退出
        if (info.size() == 0 || page == getRuntime().getParameters().get('sizePage')) {
            log.info("任务结束，页码 :{} ", page)
            return
        }


        info.stream().filter((it) -> {
            return checkTime(it['time'])
        }).findFirst().ifPresentOrElse((it) -> {
            log.info("任务结束，页码 :{} ", page)
        }, () -> {
            analysisWebContent(result, host, cookie, page + 1)
        })


    }
    HttpAction httpAction = null

    @Override
    Object run() {
        httpAction = action(HttpAction.class)
        def parameters = getRuntime().getParameters()
        MatchWordAction action = action(MatchWordAction.class).build('微博',parameters['collectionNames'])
        def cookie = parameters['cookie']
        def host = parameters['host']
        def isDel = parameters['isDel']
        def result = new HashSet()
        httpAction.setCookies(cookie)
        analysisWebContent(result, host, cookie, 1)
        long weightValue = 0L
        if (result.size() > 0){
            result.forEach((it)->{
                println(it['text'])
                weightValue = weightValue + action.match(host,it['text'])
            })
        }
        if (weightValue > 0){
            action.save(host,weightValue)
        }
        if (isDel){
            taskService.del(getRuntime().getTaskId())
        }
        return [
                'time': System.currentTimeMillis()
        ]

    }


    def checkTime(String time) {
        String[] year = getRuntime().getParameters().get('year')
        if (time.indexOf('-') > 0) {
            for (int i = 0; i < year.length; i++) {
                if (time.indexOf(year[i]) > -1) {
                    return false
                }
            }
        } else {
            return false
        }
        return true
    }

    def connect(def url, def cookie, def page) {
        url = url + '&page=' + page
        return httpAction.get(url).parse().html()
    }



}
