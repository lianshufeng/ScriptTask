package top.dzurl.task.bridge.helper;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.dzurl.task.bridge.script.SuperScript;

@Slf4j
@Component
public class ScriptHelper {


    @Autowired
    private SpringBeanHelper springBeanHelper;


    /**
     * 载入脚本
     *
     * @param scriptContent
     * @return
     */
    public SuperScript parse(String scriptContent) {
        Script script = new GroovyShell().parse(scriptContent);
        if (!(script instanceof SuperScript)) {
            throw new RuntimeException("脚本转换异常");
        }
        return (SuperScript) script;
    }


}
