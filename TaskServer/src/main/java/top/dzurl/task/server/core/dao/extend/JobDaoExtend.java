package top.dzurl.task.server.core.dao.extend;

import top.dzurl.task.bridge.model.param.JobParam;
import top.dzurl.task.server.core.domain.Job;

import java.util.List;

public interface JobDaoExtend {

    List<Job> get(JobParam param);
}
