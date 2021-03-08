package top.dzurl.task.server.core.dao.extend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import top.dzurl.task.server.core.domain.Script;

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


}
