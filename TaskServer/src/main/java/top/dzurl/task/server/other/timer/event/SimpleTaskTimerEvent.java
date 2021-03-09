package top.dzurl.task.server.other.timer.event;


import top.dzurl.task.server.other.mongo.domain.SuperEntity;

@FunctionalInterface
public interface SimpleTaskTimerEvent<T extends SuperEntity> {

    /**
     * 执行
     *
     * @param entity
     */
    void execute(T entity);
}
