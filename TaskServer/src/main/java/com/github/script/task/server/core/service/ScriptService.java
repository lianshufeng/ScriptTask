package com.github.script.task.server.core.service;

import com.github.script.task.bridge.model.ScriptModel;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import com.github.script.task.server.core.dao.mongo.ScriptDao;
import com.github.script.task.server.core.domain.Script;
import com.github.script.task.server.other.mongo.util.PageEntityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 删除
     *
     * @param scriptName
     * @return
     */
    public ResultContent<ResultState> del(String scriptName) {
        return scriptDao.del(scriptName) ? ResultContent.buildContent(ResultState.Success) : ResultContent.buildContent(ResultState.Fail);
    }


    /**
     * 检查脚本版本
     *
     * @param scriptName
     * @param hash
     * @return
     */
    public ResultContent<String> check(String scriptName, String hash) {
        if (!scriptDao.existsByName(scriptName)) {
            return ResultContent.build(ResultState.ScriptNotExists);
        }
        Script script = scriptDao.findByName(scriptName);
        return script.getHash().equals(hash) ? ResultContent.build(ResultState.Success) :
                ResultContent.build(ResultState.ScriptNotSameVersion, script.getBody());
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

    /**
     * 获取脚本
     *
     * @param scriptName
     * @return
     */
    public ScriptModel get(String scriptName) {
        return toModel(scriptDao.findByName(scriptName));
    }


}
