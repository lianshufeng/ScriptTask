package com.github.script.task.server.core.dao.extend;

import com.github.script.task.server.core.domain.JobRunLog;

import java.util.List;

public interface JobRunLogDaoExtend {


    Boolean update(JobRunLog jobRunLog);


    /**
     * 追加Job的运行日志
     *
     * @param jobId
     * @param info
     */
    boolean appendLogs(String jobId, List<String> info);


}
