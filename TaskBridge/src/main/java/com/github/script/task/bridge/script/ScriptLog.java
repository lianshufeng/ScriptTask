package com.github.script.task.bridge.script;

import com.github.script.task.bridge.service.LogService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.script.task.bridge.util.JsonUtil;

import java.util.HashMap;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScriptLog {
    //log
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SuperScript.class);

    //当前脚本
    protected SuperScript script;

    @Autowired
    private LogService logService;


    public void debug(String msg) {
        log.debug(msg);
        postLog("debug", msg);
    }

    public void debug(String format, Object... arguments) {
        log.debug(format, arguments);
        postLog("debug", format, arguments);
    }


    public void error(String msg) {
        log.error(msg);
        postLog("error", msg);
    }

    public void error(String format, Object... arguments) {
        log.error(format, arguments);
        postLog("error", format, arguments);
    }

    public void info(String msg) {
        log.info(msg);
        postLog("info", msg);
    }

    public void info(String format, Object... arguments) {
        log.info(format, arguments);
        postLog("info", format, arguments);
    }


    private void postLog(String level, String format, Object... arguments) {
        String ret = String.format(format.replaceAll("\\{}", "%s"), arguments);
        this.postLog(level, ret);
    }


    /**
     * 上报日志
     *
     * @param level
     * @param msg
     */
    private void postLog(String level, String msg) {
        String jobId = this.script.getRuntime().getJobId();
        logService.info(jobId == null ? "" : jobId, JsonUtil.toJson(new HashMap<String, Object>() {{
            put("level", level);
            put("msg", msg);
        }}));
    }
}
