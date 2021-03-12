package top.dzurl.task.client.core.controller;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.dzurl.task.bridge.model.ScriptRunTimeModel;
import top.dzurl.task.bridge.service.ScriptService;
import top.dzurl.task.client.core.util.ApplicationHomeUtil;

import java.io.File;

@RestController
@RequestMapping("script")
public class ScriptController {

    @Autowired
    private ScriptService scriptService;

    @SneakyThrows
    @RequestMapping("run")
    public Object run(String name) {
        File scriptHomeFile = ApplicationHomeUtil.getResource("script");
        String code = FileUtils.readFileToString(new File(scriptHomeFile.getAbsolutePath() + "/" + name), "UTF-8");
        return scriptService.executeScript(code, new ScriptRunTimeModel());
    }


}
