package com.github.script.task.bridge.script;

import com.github.script.task.bridge.helper.Where;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import com.github.script.task.bridge.helper.SpringBeanHelper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScriptAsync {

    //当前脚本
    protected SuperScript script;

    //调度器线程池
    private ScheduledExecutorService threadPool;

    //阻塞线程
    private CountDownLatch countDownLatch;

    @Autowired
    private SpringBeanHelper springBeanHelper;


    /**
     * 初始化
     *
     * @return
     */
    @Autowired
    private void init(ApplicationContext applicationContext) {
        threadPool = this.script.getRuntime().getThreadPool();
        countDownLatch = new CountDownLatch(1);
    }


    /**
     * 异步流程
     *
     * @param <T>
     * @return
     */
    public <T> Where where(boolean cycle) {
        return new Where(this.threadPool, cycle);
    }


    /**
     * 异步流程
     *
     * @param <T>
     * @return
     */
    public <T> Where where() {
        return this.where(false);
    }


    /**
     * 暂停
     */
    @SneakyThrows
    public void await() {
        countDownLatch.await();
    }

    /**
     * 暂停
     */
    @SneakyThrows
    public void await(long time) {
        countDownLatch.await(time, TimeUnit.MILLISECONDS);
    }

    /**
     * 继续，必须在await之后执行
     */
    public void proceed() {
        countDownLatch.countDown();
    }

}
