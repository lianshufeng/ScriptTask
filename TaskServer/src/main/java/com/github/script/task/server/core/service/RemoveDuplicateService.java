package com.github.script.task.server.core.service;

import com.github.script.task.bridge.conf.RemoveDuplicateConf;
import com.github.script.task.bridge.model.param.RemoveDuplicateParam;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import com.github.script.task.server.core.dao.RemoveDuplicateDao;
import com.github.script.task.server.core.domain.RemoveDuplicate;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RemoveDuplicateService {


    @Autowired
    private RemoveDuplicateDao removeDuplicateDao;

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private RemoveDuplicateConf removeDuplicateConf;

    /**
     * 检查去重
     * @param param
     * @return
     */
    public ResultContent<List<String>> duplicateAndSave(RemoveDuplicateParam param){
        List<RemoveDuplicate> saveList = new ArrayList<>();
        List<String> remove = new ArrayList<>();
        //查询去重
        List<RemoveDuplicate> queryList = removeDuplicateDao.findByScriptNameAndValueIn(param.getScriptName(),param.getValues());
        queryList.forEach((it) ->{
                if (param.getValues().contains(it.getValue())){
                    remove.add(it.getValue());
                }
        });
        if (remove.size() > 0){
            param.getValues().removeAll(remove);
        }
        if (param.getValues().size() > 0){
            param.getValues().forEach((it -> {
                RemoveDuplicate removeDuplicate = new RemoveDuplicate();
                removeDuplicate.setScriptName(param.getScriptName());
                removeDuplicate.setValue(it);
                removeDuplicate.setTtl(new Date(dbHelper.getTime() + removeDuplicateConf.getDefaultTTl()));
                saveList.add(removeDuplicate);
            }));
        }
        if (saveList.size() > 0){
            return ResultContent.buildContent(removeDuplicateDao.saveAll(saveList).stream().map((it) -> {
                return it.getValue();
            }).collect(Collectors.toList()));
        }
        return ResultContent.build(ResultState.Fail);
    }
}
