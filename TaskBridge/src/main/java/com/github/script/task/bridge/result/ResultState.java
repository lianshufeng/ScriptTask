package com.github.script.task.bridge.result;

import lombok.Getter;

/**
 * 结果状态模板
 */
public enum ResultState {

    Success("成功"),
    Fail("失败"),
    Error("错误"),
    Exception("异常"),
    Robot("机器验证"),
    TaskNoneExists("任务不存在"),
    ScriptNotExists("脚本不存在"),
    ScriptNotSameVersion("脚本版本不一致"),
    CronError("表达式错误"),
    JobLogNoneExists("日志不存在"),
    JobNotExists("工作不存在");

    @Getter
    private String remark;

    ResultState(String remark) {
        this.remark = remark;
    }
}
