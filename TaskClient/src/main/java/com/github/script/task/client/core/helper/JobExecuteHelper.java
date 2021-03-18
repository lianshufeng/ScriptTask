package com.github.script.task.client.core.helper;

import com.github.script.task.bridge.conf.ScriptTaskConf;
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
    private ScriptTaskConf scriptTaskConf;

    @Autowired
    private ScriptService scriptService;

    //脚本的存放目录
    private File scriptHome = null;

    @Autowired
    private void init(ApplicationContext applicationContext) {
        this.scriptHome = this.scriptTaskConf.getRunTime().getScriptHome();
    }

    /**
     * 执行Job
     *
     * @param job
     */
    public void execute(JobModel job) {

        //获取脚本名
        String scriptName = job.getScriptName();

        //载入脚本代码
        String scriptCode = loadScript(scriptName);

        //执行脚本
        this.scriptService.executeScript(scriptCode, job);

    }

    /**
     * 载入脚本
     *
     * @param scriptName
     * @return
     */
    @SneakyThrows
    private String loadScript(final String scriptName) {
        //读取磁盘上的脚本
        byte[] scriptBin = loadDiskScript(scriptName);

        //hash
        String hash = scriptBin == null ? null : DigestUtils.md5DigestAsHex(scriptBin);

        //检查或者更新脚本
        ResultContent<String> resultContent = scriptService.checkOrUpdate(scriptName, hash);
        if (resultContent.getState() != ResultState.Success) {
            return scriptBin == null ? null : toScriptText(scriptBin);
        }


        //服务端返回的最新的脚本,base64编码
        if (resultContent.getContent() != null) {
            log.info("[脚本] - [更新] - [{}]", scriptName);
            scriptBin = Base64.getDecoder().decode(resultContent.getContent());
            saveDiskScript(scriptName, scriptBin);
        }

        return toScriptText(scriptBin);
    }


    /**
     * 转换为脚本文本
     *
     * @param scriptBin
     * @return
     */
    @SneakyThrows
    private String toScriptText(byte[] scriptBin) {
        return new String(scriptBin, "UTF-8");
    }


    /**
     * 载入脚本
     *
     * @param scriptName
     * @return
     */
    @SneakyThrows
    private byte[] loadDiskScript(String scriptName) {
        //脚本文件
        File scriptFile = scriptFile(scriptName);
        if (scriptFile == null || scriptFile.exists() == false) {
            return null;
        }
        return FileUtils.readFileToByteArray(scriptFile);
    }


    /**
     * 保存到磁盘上
     *
     * @param scriptName
     */
    @SneakyThrows
    private void saveDiskScript(String scriptName, byte[] bin) {
        File scriptFile = scriptFile(scriptName);
        FileUtils.writeByteArrayToFile(scriptFile, bin);
    }


    /**
     * 脚本的路径
     *
     * @param scriptName
     * @return
     */
    private File scriptFile(String scriptName) {
        return new File(this.scriptHome.getAbsolutePath() + "/" + scriptName);
    }


}
