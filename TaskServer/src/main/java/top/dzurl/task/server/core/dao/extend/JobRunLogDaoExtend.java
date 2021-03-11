package top.dzurl.task.server.core.dao.extend;

import top.dzurl.task.server.core.domain.JobRunLog;

import java.util.List;

public interface JobRunLogDaoExtend {


    Boolean update(JobRunLog jobRunLog);


    /**
     * 追加Job的运行日志
     *
     * @param jobId
     * @param info
     */
    boolean appendInfo(String jobId, List<String> info);


}
