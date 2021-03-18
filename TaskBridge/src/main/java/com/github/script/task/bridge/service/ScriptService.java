package com.github.script.task.bridge.service;

import com.github.script.task.bridge.result.ResultContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.script.task.bridge.model.ScriptRunTimeModel;
import com.github.script.task.bridge.script.ScriptFactory;
import com.github.script.task.bridge.script.SuperScript;

import java.util.HashMap;

@Service
public class ScriptService extends SuperService {

    @Autowired
    private ScriptFactory scriptFactory;


    /**
     * 检查或更新脚本
     *
     * @param scriptName
     * @param hash
     * @return
     */
    public ResultContent<String> checkOrUpdate(String scriptName, String hash) {
        return postForm("/script/check", new HashMap<String, Object>() {{
            put("x", 1);
        }}, ResultContent.class);
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
