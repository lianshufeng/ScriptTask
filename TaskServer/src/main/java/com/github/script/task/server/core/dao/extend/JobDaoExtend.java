package com.github.script.task.server.core.dao.extend;

import com.github.script.task.server.core.domain.Job;
import com.github.script.task.bridge.model.param.JobParam;

import java.util.List;

public interface JobDaoExtend {

    List<Job> get(JobParam param);
}
