package top.dzurl.task.client.core.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import top.dzurl.task.bridge.model.ScriptModel;
import top.dzurl.task.bridge.result.ResultContent;

public interface ScriptService {

    /**
     * 添加脚本
     *
     * @param model
     * @return
     */
    @RequestMapping("add")
    ResultContent<String> add(@RequestBody ScriptModel model);


}
