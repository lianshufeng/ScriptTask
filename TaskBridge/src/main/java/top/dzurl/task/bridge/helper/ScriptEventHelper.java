package top.dzurl.task.bridge.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.dzurl.task.bridge.script.ScriptEvent;
import top.dzurl.task.bridge.script.SuperScript;

import java.util.Optional;

@Slf4j
@Component
public class ScriptEventHelper {

    /**
     * 事件发布
     */
    public void publish(SuperScript script, ScriptEvent.EventType eventType, Object... args) {
        Optional.ofNullable(script.event()).ifPresent((it) -> {
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
