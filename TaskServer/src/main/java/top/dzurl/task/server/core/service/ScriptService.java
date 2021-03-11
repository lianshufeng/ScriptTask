package top.dzurl.task.server.core.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.dzurl.task.bridge.model.ScriptModel;
import top.dzurl.task.bridge.result.ResultContent;
import top.dzurl.task.bridge.result.ResultState;
import top.dzurl.task.server.core.dao.ScriptDao;
import top.dzurl.task.server.core.domain.Script;
import top.dzurl.task.server.other.mongo.util.PageEntityUtil;

import java.util.Base64;

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
     * @param scriptName
     * @return
     */
    public ResultContent<ResultState> del(String scriptName){
        return scriptDao.del(scriptName) ? ResultContent.buildContent(ResultState.Success) : ResultContent.buildContent(ResultState.Fail);
    }


    /**
     * 检查脚本版本
     * @param scriptName
     * @param hash
     * @return
     */
    public ResultContent<String> check(String scriptName, String hash) {
        if (scriptDao.existsByName(scriptName)){
            return ResultContent.build(ResultState.ScriptNoneExists);
        }
        Script script = scriptDao.findByName(scriptName);
        return script.getHash().equals(hash) ? ResultContent.build(ResultState.Success) :
                ResultContent.build(ResultState.ScriptNotSameVersion,Base64.getEncoder().encode(script.getBody()));
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
