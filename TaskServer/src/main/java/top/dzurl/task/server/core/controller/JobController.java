package top.dzurl.task.server.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.dzurl.task.server.core.service.JobService;

@RestController
@RequestMapping("job")
public class JobController {

    @Autowired
    private JobService jobService;

    @RequestMapping("get")
    public Object get(String deviceId){
        return jobService.get(deviceId);
    }
}
