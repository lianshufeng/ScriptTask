package top.dzurl.task.bridge.script;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 脚本运行环境
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScriptRuntime {
    //环境
    private Environment environment;

    //参数
    private Map<String, Object> parameters;

    //驱动
    private Object driver;

    //线程池
    protected ScheduledExecutorService threadPool;

}
