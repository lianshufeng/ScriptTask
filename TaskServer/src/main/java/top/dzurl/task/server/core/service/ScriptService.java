package top.dzurl.task.server.core.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.dzurl.task.bridge.model.ScriptModel;
import top.dzurl.task.server.core.dao.ScriptDao;
import top.dzurl.task.server.core.domain.Script;
import top.dzurl.task.server.core.util.PageEntityUtil;

@Service
public class ScriptService {

    @Autowired
    private ScriptDao scriptDao;


    /**
     * 更新脚本
     *
     * @return
     */
    @Transactional
    public ScriptModel put(byte[] buffer) {
        return toModel(this.scriptDao.put(buffer));
    }


    /**
     * 列表
     *
     * @return
     */
    public Page<ScriptModel> list(String word, Pageable pageable) {
        return PageEntityUtil.concurrent2PageModel(this.scriptDao.list(word, pageable), (it) -> {
            return toModel(it);
        });
    }


    /**
     * 转换到模型
     *
     * @param script
     * @return
     */
    public ScriptModel toModel(Script script) {
        if (script == null) {
            return null;
        }
        ScriptModel model = new ScriptModel();
        BeanUtils.copyProperties(script, model, "body");
        return model;
    }


}
