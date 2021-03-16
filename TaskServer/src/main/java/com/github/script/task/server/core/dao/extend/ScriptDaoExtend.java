package com.github.script.task.server.core.dao.extend;

import com.github.script.task.server.core.domain.Script;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScriptDaoExtend {

    /**
     * 更新脚本
     *
     * @return
     */
    Script put(byte[] buffer);


    /**
     * 分页条件查询
     *
     * @param word
     * @param pageable
     * @return
     */
    Page<Script> list(String word, Pageable pageable);


    /**
     * 删除脚本
     * @param scriptName
     * @return
     */
    Boolean del(String scriptName);


}
