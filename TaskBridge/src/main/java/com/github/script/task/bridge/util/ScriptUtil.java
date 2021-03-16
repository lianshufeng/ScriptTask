package com.github.script.task.bridge.util;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import com.github.script.task.bridge.script.SuperScript;

@Slf4j
public class ScriptUtil {


    /**
     * 转换脚本为对象
     *
     * @param scriptContent
     * @return
     */
    public static SuperScript parse(String scriptContent) {
        Script script = new GroovyShell().parse(scriptContent);
        if (!(script instanceof SuperScript)) {
            throw new RuntimeException("脚本转换异常");
        }
        return (SuperScript) script;
    }


}
