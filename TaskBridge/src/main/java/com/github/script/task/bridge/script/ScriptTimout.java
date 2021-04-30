package com.github.script.task.bridge.script;


import com.github.script.task.bridge.helper.ScriptEventHelper;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
public class ScriptTimout {

    //脚本
    private SuperScript script;

    //过期时间
    private long aliveTime;

    //同步阻塞器
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    //超时时间
    private long timeout;


    private Future runThreadFuture = null;


    @Autowired
    private ScriptEventHelper scriptEventHelper;

    /**
     * 构造方法
     *
     * @param script
     */
    public ScriptTimout(SuperScript script) {
        this.script = script;
        this.script.scriptTimout = this;
        this.timeout = script.getRuntime().getEnvironment().getTimeout();
    }


    /**
     * 执行脚本
     */
    public Object execute() {
        heartbeat();
        //脚本的线程池
        ScheduledExecutorService threadPoolExecutor = script.getRuntime().getThreadPool();

        //监视是否超时
        listenTimeOutThread(threadPoolExecutor);

        //调用脚本并返回结果
        Object ret = runScript(threadPoolExecutor);

        //释放资源
        release();

        return ret;
    }

    //释放
    private void release() {
        //关闭线程
        Optional.ofNullable(runThreadFuture).ifPresent((it) -> {
            if (!it.isCancelled()) {
                it.cancel(true);
            }
        });

    }

    /**
     * 运行脚本
     *
     * @return
     */
    @SneakyThrows
    private Object runScript(ScheduledExecutorService threadPoolExecutor) {
        final AtomicReference<Object> ret = new AtomicReference();
        runThreadFuture = threadPoolExecutor.submit(() -> {
            try {
                ret.set(script.execute());
            } catch (Exception e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        });

        //等待线程结束
        await();

        return ret.get();
    }


    private void listenTimeOutThread(final ScheduledExecutorService threadPoolExecutor) {
        threadPoolExecutor.schedule(() -> {
            long time = System.currentTimeMillis() - aliveTime;
            if (time > 0 && countDownLatch != null) {
                script.log.error("执行超时 : {}", (time + timeout) + " ms");
                scriptEventHelper.publish(script, ScriptEvent.EventType.onInterrupt);
                countDownLatch.countDown();
            } else {
                listenTimeOutThread(threadPoolExecutor);
            }
        }, 1, TimeUnit.SECONDS);
    }


    /**
     * 心跳
     */
    public void heartbeat() {
        this.aliveTime = System.currentTimeMillis() + this.timeout;
        log.debug("heartbeat : {} ", this.aliveTime);
    }

    @SneakyThrows
    private void await() {
        countDownLatch.await();
    }

}
