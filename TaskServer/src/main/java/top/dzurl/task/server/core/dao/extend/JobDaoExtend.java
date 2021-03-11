package top.dzurl.task.server.core.dao.extend;

import top.dzurl.task.bridge.model.param.JobParam;
import top.dzurl.task.server.core.domain.Job;

public interface JobDaoExtend {

    Job get(JobParam param);
}
