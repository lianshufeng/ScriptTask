package top.dzurl.task.server.core.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import top.dzurl.task.bridge.model.TaskModel;
import top.dzurl.task.bridge.model.param.TaskParam;
import top.dzurl.task.bridge.result.ResultContent;
import top.dzurl.task.bridge.result.ResultState;
import top.dzurl.task.server.core.dao.ScriptDao;
import top.dzurl.task.server.core.dao.TaskDao;
import top.dzurl.task.server.core.domain.Script;
import top.dzurl.task.server.core.domain.Task;
import top.dzurl.task.server.other.mongo.util.PageEntityUtil;

@Service
public class TaskService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ScriptDao scriptDao;

    /**
     * 创建任务
     * @param param
     * @return
     */
    public ResultContent<String> create(TaskParam param){
        //检验表达式
        try {
            CronExpression.parse(param.getCron());
        } catch (Exception e){
            return ResultContent.build(ResultState.CronError);
        }
        //查询脚本
        if (!this.scriptDao.existsByName(param.getScriptName())){
            return ResultContent.build(ResultState.ScriptNoneExists);
        }
        Script script = this.scriptDao.findByName(param.getScriptName());
        Task task = new Task();
        task.setCron(param.getCron());
        task.setScriptName(script.getName());
        BeanUtils.copyProperties(script,task);
        //入库
        this.taskDao.save(task);
        return ResultContent.buildContent(this.taskDao.save(task).getId());
    }


    /**
     * 修改任务
     * @param param
     * @return
     */
    public ResultContent<ResultState> update(TaskParam param){
        return taskDao.update(param)?ResultContent.buildContent(ResultState.Success):ResultContent.buildContent(ResultState.Fail);
    }

    /**
     * 任务列表
     * @param param
     * @param pageable
     * @return
     */
    public Page<TaskModel> list(TaskParam param,Pageable pageable){
        return PageEntityUtil.concurrent2PageModel(this.taskDao.list(param, pageable), (it) -> {
            return toModel(it);
        });
    }


    /**
     * 删除任务
     * @param id
     * @return
     */
    public ResultContent<ResultState> del(String id){
        return taskDao.del(id)?ResultContent.buildContent(ResultState.Success):ResultContent.buildContent(ResultState.Fail);
    }




    /**
     * 转换到模型
     *
     * @param task
     * @return
     */
    public TaskModel toModel(Task task) {
        if (task == null) {
            return null;
        }
        TaskModel model = new TaskModel();
        BeanUtils.copyProperties(task, model);
        return model;
    }
}
