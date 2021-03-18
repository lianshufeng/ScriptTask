package com.github.script.task.client.core.helper;

import com.github.script.task.bridge.conf.ScriptTaskConf;
import com.github.script.task.bridge.helper.ScriptStoreHelper;
import com.github.script.task.bridge.model.JobModel;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import com.github.script.task.bridge.script.ScriptFactory;
import com.github.script.task.bridge.service.ScriptService;
import com.github.script.task.bridge.util.HashUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.util.Base64;

/**
 * 任务执行助手
 */
@Slf4j
@Component
public class JobExecuteHelper {

    @Autowired
    private ScriptService scriptService;


    @Autowired
    private ScriptStoreHelper scriptStoreHelper;


    /**
     * 执行Job
     *
     * @param job
     */
    public void execute(JobModel job) {

        //获取脚本名
        String scriptName = job.getScriptName();

        //载入脚本代码
        String scriptCode = scriptStoreHelper.loadScript(scriptName);

        //执行脚本
        this.scriptService.executeScript(scriptCode, job);

    }


}
