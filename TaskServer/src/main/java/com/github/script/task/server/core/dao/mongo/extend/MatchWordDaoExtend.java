package com.github.script.task.server.core.dao.mongo.extend;

import com.github.script.task.bridge.model.param.MatchWordParam;
import com.github.script.task.server.core.domain.MatchWord;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface MatchWordDaoExtend {


    boolean del(String id);

    boolean update(MatchWordParam param);

    Page<MatchWord> list(MatchWordParam param, Pageable pageable);


    /**
     * 获取所有名称
     *
     * @return
     */
    Set<String> collectionNames();


}
