package top.dzurl.task.bridge.script;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.dzurl.task.bridge.model.ScriptRunTimeModel;

import java.util.concurrent.ScheduledExecutorService;

/**
 * 脚本运行环境
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ScriptRuntime extends ScriptRunTimeModel {

    //驱动
    private Object driver;

    //线程池
    protected ScheduledExecutorService threadPool;

    //当前脚本
    protected SuperScript _script;

}
