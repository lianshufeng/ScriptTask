package com.github.script.task.server.core.service;

import com.github.script.task.bridge.model.param.UpdateTaskParam;
import com.github.script.task.bridge.script.Parameter;
import com.github.script.task.bridge.script.ParameterType;
import com.github.script.task.bridge.util.BeanUtil;
import com.github.script.task.bridge.util.ConvertUtil;
import com.github.script.task.server.core.conf.TTLConf;
import com.github.script.task.server.core.dao.ScriptDao;
import com.github.script.task.server.core.dao.TaskDao;
import com.github.script.task.server.core.domain.Script;
import com.github.script.task.server.core.domain.Task;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import com.github.script.task.server.other.mongo.util.PageEntityUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import com.github.script.task.bridge.model.TaskModel;
import com.github.script.task.bridge.model.param.TaskParam;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ScriptDao scriptDao;

    @Autowired
    private TTLConf ttlConf;

    @Autowired
    private DBHelper dbHelper;


    /**
     * 创建任务
     *
     * @param param
     * @return
     */
    public ResultContent<String> create(TaskParam param) {
        //检验表达式
        if (!StringUtils.isBlank(param.getCron())) {
            try {
                CronExpression.parse(param.getCron());
            } catch (Exception e) {
                return ResultContent.build(ResultState.CronError);
            }
        }
        //查询脚本
        if (!this.scriptDao.existsByName(param.getScriptName())) {
            return ResultContent.build(ResultState.ScriptNotExists);
        }
        Script script = this.scriptDao.findByName(param.getScriptName());
        Task task = new Task();
        BeanUtils.copyProperties(script, task, "id");
        task.setCron(param.getCron());
        task.setScriptName(script.getName());
        task.setParameters(converParam(script.getParameters(),param.getParameters()));
        task.setRemark(script.getRemark());
        if (param.getTimeout() == null) {
            task.setTtl(new Date(dbHelper.getTime() + ttlConf.getTaskTimeOut()));
        } else {
            Assert.isTrue(param.getTimeout() > dbHelper.getTime(),"过期时间不能小于当前时间");
            task.setTtl(new Date(param.getTimeout()));
        }
        //入库
        return ResultContent.buildContent(toModel(this.taskDao.save(task)));
    }

    Map<String, Object> converParam(Map<String, ParameterType> source, Map<String, Object> parameters){
        if (parameters == null && parameters.size() == 0){
            return null;
        }
        if (source == null && source.size() == 0){
            return null;
        }
        Map<String,Object> param = new HashMap<>();
        parameters.forEach((k,v)->{
            ParameterType parameterType = source.get(k);
            if (parameterType != null){
                try {
                    param.put(k, ConvertUtil.conver(Class.forName(parameterType.getType()),String.valueOf(v)));
                } catch (Exception e){
                    throw new RuntimeException("参数转换失败！");
                }

            }
        });
        return param;
    }


    /**
     * 修改任务
     *
     * @param param
     * @return
     */
    public ResultContent<ResultState> update(UpdateTaskParam param) {

        Optional<Task> optional = taskDao.findById(param.getId());

        if (!optional.isPresent()) {
            return ResultContent.buildContent(ResultState.TaskNoneExists);
        }
        Task old = optional.get();


        //合并参数
        if (param.getEnvironment() != null && param.getEnvironment().getDevice() != null) {
            param.setDevice(merge(BeanUtil.bean2Map(old.getEnvironment().getDevice()), BeanUtil.bean2Map(param.getEnvironment().getDevice())));
        }
        if (param.getParameters() != null) {
            Script script = scriptDao.findByName(old.getScriptName());
            param.setParameters(merge(old.getParameters(), converParam(script.getParameters(),param.getParameters())));
        }
        if (param.getTimeout() != null) {
            Assert.isTrue(param.getTimeout() > dbHelper.getTime(),"过期时间不能小于当前时间");
            param.setTtl(new Date(param.getTimeout()));
        }
        return taskDao.update(param) ? ResultContent.buildContent(ResultState.Success) : ResultContent.buildContent(ResultState.Fail);
    }

    private Map<String, Object> merge(Map<String, Object> m1, Map<String, Object> m2) {
        Map<String, Object> result = new HashMap<>();
        if (m1 != null) {
            result.putAll(m1);
        }
        m2.forEach((key, value) -> {
            if (value != null) {
                result.put(key, value);
            }
        });
        return result;
    }


    /**
     * 任务列表
     *
     * @param param
     * @param pageable
     * @return
     */
    public Page<TaskModel> list(TaskParam param, Pageable pageable) {
        return PageEntityUtil.concurrent2PageModel(this.taskDao.list(param, pageable), (it) -> {
            return toModel(it);
        });
    }


    /**
     * 删除任务
     *
     * @param id
     * @return
     */
    public ResultContent<ResultState> del(String id) {
        return taskDao.del(id) ? ResultContent.buildContent(ResultState.Success) : ResultContent.buildContent(ResultState.Fail);
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
        model.setTimeout(task.getTtl().getTime());
        return model;
    }

    public TaskModel findById(String id) {
        return toModel(taskDao.findById(id).get());
    }
}
