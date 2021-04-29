package com.github.script.task.bridge.script;


import lombok.Cleanup;
import lombok.SneakyThrows;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;


public class ScriptTimout {

    //脚本
    private SuperScript script;

    //过期时间
    private long aliveTime;

    //同步阻塞器
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    //超时时间
    private long timeout;


    private ScheduledFuture listenScheduledFuture = null;
    private Future runThreadFuture = null;


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
        //关键线程池

        @Cleanup("shutdownNow") ScheduledExecutorService threadPoolExecutor = Executors.newScheduledThreadPool(2);

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
        //释放监视调度器
        Optional.ofNullable(listenScheduledFuture).ifPresent((it) -> {
            if (!it.isCancelled()) {
                it.cancel(true);
            }
        });

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


    private void listenTimeOutThread(ScheduledExecutorService threadPoolExecutor) {
        listenScheduledFuture = threadPoolExecutor.scheduleAtFixedRate(() -> {
            long time = System.currentTimeMillis() - aliveTime;
            if (time > 0) {
                Optional.ofNullable(countDownLatch).ifPresent((it) -> {
                    listenScheduledFuture.cancel(true);
                    script.log.error("执行超时 : {}", (time + timeout) + " ms");
                    it.countDown();
                });
            }
        }, 1, 1, TimeUnit.SECONDS);
    }


    /**
     * 心跳
     */
    public void heartbeat() {
        this.aliveTime = System.currentTimeMillis() + this.timeout;
    }

    @SneakyThrows
    private void await() {
        countDownLatch.await();
    }

}
