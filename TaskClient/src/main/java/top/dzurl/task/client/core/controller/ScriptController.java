package top.dzurl.task.client.core.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
    public Object run(@RequestBody RunTaskModel model) {
        final String scriptName = model.getName();
        File scriptHomeFile = ApplicationHomeUtil.getResource("script");
        String code = FileUtils.readFileToString(new File(scriptHomeFile.getAbsolutePath() + "/" + scriptName), "UTF-8");
        return scriptService.executeScript(code, model);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RunTaskModel extends ScriptRunTimeModel {

        //脚本名
        private String name;


    }


}
