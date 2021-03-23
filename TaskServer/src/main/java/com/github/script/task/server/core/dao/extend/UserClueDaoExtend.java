package com.github.script.task.server.core.dao.extend;

import com.github.script.task.bridge.model.param.UserClueParam;
import com.github.script.task.server.core.domain.UserClue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserClueDaoExtend {
    Page<UserClue> list(UserClueParam param, Pageable pageable);
}
