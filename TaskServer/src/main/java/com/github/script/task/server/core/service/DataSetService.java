package com.github.script.task.server.core.service;


import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import com.github.script.task.bridge.util.TextHashUtil;
import com.github.script.task.server.core.dao.DataSetDao;
import com.github.script.task.server.core.domain.DataSet;
import com.github.script.task.server.core.model.DataSetModel;
import com.github.script.task.server.core.service.data.DataHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DataSetService {

    @Autowired
    private DataSetDao dataSetDao;


    @Autowired
    private List<DataHandle> dataHandleList;


    /**
     * 推送数据
     *
     * @param text
     * @return
     */
    public ResultContent<DataSetModel> push(String text) {
        String hash = TextHashUtil.hash(text);
        DataSet dataSet = dataSetDao.findByHash(hash);
        if (dataSet != null) {
            return ResultContent.buildContent(toModel(dataSet));
        }
        dataSet = DataSet.builder().hash(hash).content(text).build();
        dataHandle(dataSet);
        return ResultContent.buildContent(toModel(dataSet));
    }


    /**
     * hash
     *
     * @param hash
     * @return
     */
    public ResultContent<DataSetModel> hash(String hash) {
        DataSet dataSet = dataSetDao.findByHash(hash);
        if (dataSet == null) {
            return ResultContent.build(ResultState.DataSetNotExists);
        }
        return ResultContent.buildContent(toModel(dataSet));
    }


    /**
     * 数据处理层
     */
    private void dataHandle(DataSet dataSet) {
        dataHandleList.forEach((handle) -> {
            handle.execute(dataSet);
        });


    }


    /**
     * 转换到模型
     *
     * @param dataSet
     * @return
     */
    private DataSetModel toModel(DataSet dataSet) {
        if (dataSet == null) {
            return null;
        }
        DataSetModel model = new DataSetModel();
        BeanUtils.copyProperties(dataSet, model);
        return model;
    }

}
