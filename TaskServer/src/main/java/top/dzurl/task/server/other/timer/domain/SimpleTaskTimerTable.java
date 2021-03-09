package top.dzurl.task.server.other.timer.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.dzurl.task.server.other.mongo.domain.SuperEntity;

/**
 * 任务定时器
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
public class SimpleTaskTimerTable extends SuperEntity {

    /**
     * cron表达式，如: 0/10 * * * * * , 每10秒执行一次
     */
    private String cron;

}
