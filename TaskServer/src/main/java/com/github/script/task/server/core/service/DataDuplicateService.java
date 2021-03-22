package com.github.script.task.server.core.service;

import com.github.script.task.bridge.model.param.DataDuplicateAndSaveParam;
import com.github.script.task.bridge.model.param.DataDuplicateParam;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import com.github.script.task.server.core.conf.TTLConf;
import com.github.script.task.server.core.dao.DataDuplicateDao;
import com.github.script.task.server.core.domain.DataDuplicate;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DataDuplicateService {


    @Autowired
    private DataDuplicateDao dataDuplicateDao;

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private TTLConf ttlConf;


    private Date buildDuplicateTTL(DataDuplicateAndSaveParam param) {
        long ttl = (param.getTtl() != null && param.getTtl() > 0) ? param.getTtl() : ttlConf.getDuplicateTimout();
        return new Date(this.dbHelper.getTime() + ttl);
    }


    /**
     * 删除数据
     *
     * @param removeDuplicateParam
     */
    public void remove(DataDuplicateParam removeDuplicateParam) {

    }


    /**
     * 检查去重
     *
     * @param param
     * @return
     */
    public ResultContent<List<String>> duplicateAndSave(DataDuplicateAndSaveParam param) {

        //查询存在的值
        Set<String> existValues = dataDuplicateDao.findByKeyAndValueIn(param.getKey(), param.getValues()).stream().map((it) -> {
            return it.getValue();
        }).collect(Collectors.toSet());


        //过滤已存在的数据,并转换为插入模型
        Set<DataDuplicate> insertDataList = param.getValues().stream().filter((it) -> {
            return !existValues.contains(it);
        }).map((it) -> {
            DataDuplicate dataDuplicate = new DataDuplicate();
            dataDuplicate.setKey(param.getKey());
            dataDuplicate.setValue(it);
            dataDuplicate.setTtl(buildDuplicateTTL(param));
            this.dbHelper.saveTime(dataDuplicate);
            return dataDuplicate;
        }).collect(Collectors.toSet());

        if (insertDataList == null || insertDataList.size() == 0) {
            return ResultContent.build(ResultState.Fail);
        }

        return ResultContent.buildContent(dataDuplicateDao.saveAll(insertDataList).stream().map((it) -> {
            return it.getValue();
        }).collect(Collectors.toList()));

    }
}
