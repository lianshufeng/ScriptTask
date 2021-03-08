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
     *
     * @param scriptContent
     * @param injection ， 是否注入spring的依赖对象
     * @return
     */
    public SuperScript parse(String scriptContent, boolean injection) {
        Script script = new GroovyShell().parse(scriptContent);
        if (!(script instanceof SuperScript)) {
            throw new RuntimeException("脚本转换异常");
        }
        SuperScript superScript = (SuperScript) script;
        if (injection) {
            this.springBeanHelper.injection(superScript);
        }
        return superScript;
    }


    /**
     * 载入脚本
     *
     * @param scriptContent
     * @return
     */
    public SuperScript parse(String scriptContent) {
        return parse(scriptContent, false);
    }


}
