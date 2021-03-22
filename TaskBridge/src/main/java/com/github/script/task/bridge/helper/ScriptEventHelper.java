package com.github.script.task.bridge.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.github.script.task.bridge.script.ScriptEvent;
import com.github.script.task.bridge.script.SuperScript;

import java.util.Optional;

@Slf4j
@Component
public class ScriptEventHelper {

    /**
     * 事件发布
     */
    public void publish(SuperScript script, ScriptEvent.EventType eventType, Object... args) {
        Optional.ofNullable(script.getRuntime().getScriptEvent()).ifPresent((it) -> {
            try {
                eventType.getMethod().invoke(it, args);
            } catch (Exception e) {
                Optional.ofNullable(e.getMessage()).ifPresent((msg) -> {
                    log.error(msg);
                });
                try {
                    ScriptEvent.EventType.Exception.getMethod().invoke(it, e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }


}
