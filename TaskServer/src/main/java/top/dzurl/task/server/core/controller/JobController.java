package top.dzurl.task.server.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.dzurl.task.bridge.model.param.JobLogParam;
import top.dzurl.task.bridge.model.param.JobParam;
import top.dzurl.task.server.core.service.JobService;

@RestController
@RequestMapping("job")
public class JobController {

    @Autowired
    private JobService jobService;

    @RequestMapping("get")
    public Object get(@RequestBody JobParam param){
        return jobService.get(param);
    }

    @RequestMapping("writeLog")
    public Object writeLog(@RequestBody JobLogParam param){
        return jobService.writeLog(param);
    }
}
