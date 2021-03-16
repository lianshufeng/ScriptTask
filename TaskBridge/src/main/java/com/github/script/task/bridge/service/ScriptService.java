package com.github.script.task.bridge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.script.task.bridge.model.ScriptRunTimeModel;
import com.github.script.task.bridge.script.ScriptFactory;
import com.github.script.task.bridge.script.SuperScript;

@Service
public class ScriptService extends SuperService {

    @Autowired
    private ScriptFactory scriptFactory;

    /**
     * 执行脚本
     *
     * @return
     */
    public Object runScript(String scriptName) {
        return null;
    }

    /**
     * 直接执行本地的脚本代码
     *
     * @return
     */
    public <T> T executeScript(final String code, final ScriptRunTimeModel runTimeModel) {
        //转换脚本
        SuperScript script = this.scriptFactory.parse(code, runTimeModel);
        //执行脚本
        Object ret = this.scriptFactory.run(script);
        //释放脚本
        this.scriptFactory.release(script);

        return ret == null ? null : (T) ret;
    }


}
