package top.dzurl.task.bridge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.dzurl.task.bridge.model.ScriptRunTimeModel;
import top.dzurl.task.bridge.script.ScriptFactory;
import top.dzurl.task.bridge.script.SuperScript;

@Service
public class ScriptService extends SuperService {

    @Autowired
    private ScriptFactory scriptFactory;

    /**
     * 将指定的脚本在本地执行
     *
     * @return
     */
    public Object runScriptOnLocal(String scriptName) {
        return null;
    }

    /**
     * 直接执行本地的脚本代码
     *
     * @return
     */
    public <T> T runScriptByCode(final String code, final ScriptRunTimeModel runTimeModel) {
        //转换脚本
        SuperScript script = scriptFactory.parse(code, runTimeModel);
        //执行脚本
        return (T) scriptFactory.run(script);
    }


}
