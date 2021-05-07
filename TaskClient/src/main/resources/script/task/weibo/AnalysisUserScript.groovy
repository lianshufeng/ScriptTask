package script.task.weibo

import com.github.script.task.bridge.device.impl.NoDevice
import com.github.script.task.bridge.script.Environment
import com.github.script.task.bridge.script.Parameter
import com.github.script.task.bridge.script.ScriptEvent
import com.github.script.task.bridge.script.SuperScript
import com.github.script.task.bridge.script.action.task.TaskAction
import com.github.script.task.bridge.script.action.net.HttpAction
import com.github.script.task.bridge.script.action.word.MatchWordAction
import com.github.script.task.bridge.service.TaskService
import com.github.script.task.bridge.util.JsonUtil
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired

import java.util.stream.Collectors

class AnalysisUserScript extends SuperScript {

    private final static String FMView = "FM.view";

    private List<String> year = new ArrayList<>()

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
                'cookie'  : new Parameter(value: 'SINAGLOBAL=4295076059692.5376.1586332527804; SSOLoginState=1620181880; wvr=6; _s_tentry=login.sina.com.cn; Apache=6687459347781.197.1620181883538; ULV=1620181883544:21:1:1:6687459347781.197.1620181883538:1619666444404; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhJ2TWaCdSW3kzQUE_eojmZ5JpX5KMhUgL.Foz4ehqp1K2pS0M2dJLoIEXLxK-L1KML1-eLxKML1-BLBK2LxKML1-2L1hBLxK-L1KqLBo-LxK-L1K5L1-zt; ALF=1651807369; SCF=Agyy7fZ5uwLCOE_SfvwDtyHWN5W-pBRELMkWd9hfLHsxQTGP4VJKsVzchvAM-LwEQKtNFdVzT2IJb64tUNdIUsg.; SUB=_2A25NlxFaDeRhGeRH61QQ-S_NzDuIHXVu5QWSrDV8PUNbmtANLVLckW9NTfMTS51BR_7BpTBFlyGXpdyoIp_P20cd; UOR=,,login.sina.com.cn; wb_view_log_2906191177=1920*10801&2560*14401'),
                'host'    : new Parameter(value: 'https://weibo.com/u/3995292155?refer_flag=1001030103_&is_ori=1',remark: '微博用户主页'),
                'sizePage': new Parameter(value: 30,remark: '检索页数（大于零的整数）'),
                'collectionNames':new Parameter(value: '搜索时间为2020年全年',remark: '词库名称（多词库用逗号隔开）'),
                'year': new Parameter(value: '2020，2021',remark: '年份')
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

    TaskAction taskAction = null

    @Override
    Object run() {
        httpAction = action(HttpAction.class)
        httpAction.setProxy(true)
        taskAction = action(TaskAction.class)
        def parameters = getRuntime().getParameters()
        String collectionNames =  parameters['collectionNames']
        List<String> list = new ArrayList<>()
        if (collectionNames.indexOf("，") > 0 || collectionNames.indexOf(",")){
            collectionNames = collectionNames.replace("，",",")
            list = Arrays.asList(collectionNames.split(","))
        } else {
            list.add(collectionNames)
        }
        String years = parameters['year']
        if (years.indexOf("，") > 0 || years.indexOf(",")){
            years = years.replace("，",",")
            year = Arrays.asList(years.split(","))
        } else {
            year.add(years)
        }
        yearArray = year.toArray(String[])
        log.info(JsonUtil.toJson(years))
        MatchWordAction action = action(MatchWordAction.class).build('微博',list)
        def cookie = parameters['cookie']
        def host = parameters['host']
        def result = new HashSet()
        httpAction.setCookies(cookie)
        analysisWebContent(result, host, cookie, 1)
        MatchWordAction.MatchResult matchResult = action.getMatchResult()
        if (result.size() > 0){
            result.forEach((it)->{
                action.match(matchResult,it['text'])
            })
        }
        if (matchResult.getWeightValue() > 0){
            action.save(host,matchResult)
        }
        log.info("删除任务：" + getRuntime().getTaskId())
        taskAction.delTask(getRuntime().getTaskId())
        return [
                'time': System.currentTimeMillis()
        ]

    }
    private String[] yearArray = null

    def checkTime(String time) {
        if (time.indexOf('-') > 0) {
            for (int i = 0; i < yearArray.length; i++) {
                if (time.indexOf(yearArray[i]) > -1) {
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
