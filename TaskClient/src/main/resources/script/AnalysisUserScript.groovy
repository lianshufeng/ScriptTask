package script

import com.github.script.task.bridge.device.impl.NoDevice
import com.github.script.task.bridge.script.Environment
import com.github.script.task.bridge.script.Parameter
import com.github.script.task.bridge.script.ScriptEvent
import com.github.script.task.bridge.script.SuperScript
import com.github.script.task.bridge.script.action.async.AsyncAction
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
                'cookie'  : new Parameter(value: 'SINAGLOBAL=4295076059692.5376.1586332527804; SSOLoginState=1616118736; _s_tentry=login.sina.com.cn; UOR=,,www.baidu.com; Apache=56067474428.02762.1616118739470; ULV=1616118739475:14:3:2:56067474428.02762.1616118739470:1615531942073; wb_view_log_2906191177=2560*14401; WBtopGlobal_register_version=2021032310; SCF=Agyy7fZ5uwLCOE_SfvwDtyHWN5W-pBRELMkWd9hfLHsxStWvfR2Iqk2hANUwXgRIQycZxg-O4k-Ir2l5tIQGqdg.; SUB=_2A25NXTwjDeRhGeRH61QQ-S_NzDuIHXVuKyrrrDV8PUNbmtAfLU7_kW9NTfMTSxp6zcFdAM9frhS6EBMTJk1XUVsf; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhJ2TWaCdSW3kzQUE_eojmZ5JpX5o275NHD95QE1K5ceK.peKMNWs4Dqcj_i--fiK.NiKLhi--NiKL2i-2pi--NiKLWiKnXi--fiK.ci-zfi--fiK.7iKLF; ALF=1617069811; un=18682630458; webim_unReadCount={"time":1616465661197,"dm_pub_total":0,"chat_group_client":0,"chat_group_notice":0,"allcountNum":39,"msgbox":0}'),
                'host'    : new Parameter(value: 'https://weibo.com/intel?refer_flag=1005055014_&is_all=1'),
                'isDel'   : new Parameter(value: true),
                'year'    : new Parameter(value: ['2020', '2021']),
                'sizePage': new Parameter(value: 10),
                'collectionNames':new Parameter(value: ['高二','高一'])
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


    @Override
    Object run() {

        def parameters = getRuntime().getParameters()
        MatchWordAction action = action(MatchWordAction.class).build(parameters['collectionNames'])
        def cookie = parameters['cookie']
        def host = parameters['host']
        def isDel = parameters['isDel']
        def result = new HashSet()

        analysisWebContent(result, host, cookie, 1)

        if (result.size() > 0){
            result.forEach((it)->{
                action.match(host,it['text'])
            })
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
        return Jsoup.connect(url).cookies(convertCookie(cookie)).get()
    }


    HashMap<String, String> convertCookie(String cookie) {
        HashMap<String, String> cookiesMap = new HashMap<String, String>()
        String[] items = cookie.trim().split(';')
        for (String item : items) cookiesMap.put(item.split('=')[0], item.split('=')[1])
        return cookiesMap
    }


}
