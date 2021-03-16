package com.github.script.task.server.other.timer.event;


import com.github.script.task.server.other.mongo.domain.SuperEntity;

@FunctionalInterface
public interface SimpleTaskTimerEvent<T extends SuperEntity> {

    /**
     * 执行
     *
     * @param entity
     */
    void execute(T entity);
}
