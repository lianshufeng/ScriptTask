package com.github.script.task.server.core.dao.extend;

import com.github.script.task.bridge.model.param.MatchWordParam;

public interface MatchWordDaoExtend {


    boolean del(String id);

    boolean update(MatchWordParam param);
}
