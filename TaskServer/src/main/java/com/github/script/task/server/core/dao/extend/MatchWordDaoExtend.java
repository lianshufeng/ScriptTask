package com.github.script.task.server.core.dao.extend;

import com.github.script.task.bridge.model.param.MatchWordParam;
import com.github.script.task.server.core.domain.MatchWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchWordDaoExtend {


    boolean del(String id);

    boolean update(MatchWordParam param);

    Page<MatchWord> list(MatchWordParam param, Pageable pageable);
}
