package top.dzurl.task.server.other.timer.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import top.dzurl.task.server.other.mongo.domain.SuperEntity;
import top.dzurl.task.server.other.timer.core.SimpleTaskTimerContainer;
import top.dzurl.task.server.other.timer.core.SimpleTaskTimerCore;
import top.dzurl.task.server.other.timer.core.SimpleTaskTimerDao;
import top.dzurl.task.server.other.timer.event.SimpleTaskTimerEvent;

/**
 * 使用本方法，的子类必须使用注解 @Configuration
 */
@Slf4j
@EnableScheduling
@ComponentScan({
        "top.dzurl.task.server.other.timer.core",
        "top.dzurl.task.server.other.timer.conf"
})
public abstract class TaskTimerConfiguration {

    @Autowired
    private ApplicationContext applicationContext;


    /**
     * 调度任务助手
     *
     * @return
     */
    @Bean
    public SimpleTaskTimerContainer simpleTaskTimerContainer() {
        SimpleTaskTimerContainer simpleTaskTimerContainer = new SimpleTaskTimerContainer();
        TaskTimerItem[] taskTimerItems = register();
        if (taskTimerItems == null) {
            return simpleTaskTimerContainer;
        }
        for (TaskTimerItem item : taskTimerItems) {
            if (item != null) {
                this.registerSimpleTaskTimerHelper(simpleTaskTimerContainer, item);
            }
        }
        return simpleTaskTimerContainer;
    }


    /**
     * 注册任务定时器
     *
     * @param simpleTaskTimerContainer
     * @param item
     */
    private void registerSimpleTaskTimerHelper(SimpleTaskTimerContainer simpleTaskTimerContainer, TaskTimerItem item) {

        //实例化数据库操作对象
        SimpleTaskTimerDao simpleTaskTimerDao = this.applicationContext.getBean(SimpleTaskTimerDao.class);
        simpleTaskTimerDao.setTaskTimerTableCls(item.getTaskTimerTable());

        //实例化核心功能
        SimpleTaskTimerCore simpleTaskTimerCore = this.applicationContext.getBean(SimpleTaskTimerCore.class);
        simpleTaskTimerCore.setTaskTimerDao(simpleTaskTimerDao);
        simpleTaskTimerCore.setTaskTimerEvent(item.getTaskTimerEvent());

        //初始化
        simpleTaskTimerCore.after();

        simpleTaskTimerContainer.add(simpleTaskTimerCore);
    }


    /**
     * 注册任务调度器
     */
    public abstract TaskTimerItem[] register();


    @Data
    @Builder
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TaskTimerItem {

        //任务定时器的表
        private Class<? extends SuperEntity> taskTimerTable;

        //任务定时器的实现类
        private SimpleTaskTimerEvent<? extends SuperEntity> taskTimerEvent;


    }

}
