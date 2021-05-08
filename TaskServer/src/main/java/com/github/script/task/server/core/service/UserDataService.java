package com.github.script.task.server.core.service;

import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import com.github.script.task.bridge.util.JsonUtil;
import com.github.script.task.server.core.dao.mongo.UserDataDao;
import com.github.script.task.server.core.domain.UserData;
import com.github.script.task.server.core.model.DataSetModel;
import com.github.script.task.server.core.model.UserDataModel;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserDataService {


    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private UserDataDao userDataDao;

    @Autowired
    private UserDataIndexService userDataIndexService;

    @Autowired
    private DBHelper dbHelper;


    /*
     *  客户端推送数据
     */
    public ResultContent<String> push(UserDataModel model) {
        final String platform = model.getPlatform();
        final String user = model.getUser();
        String hash = null;
        if (StringUtils.hasText(model.getHash())) {
            ResultContent<DataSetModel> ret = dataSetService.hash(model.getHash());
            if (ret.getState() != ResultState.Success) {
                return ResultContent.build(ret.getState());
            }
            hash = ret.getContent().getHash();
        } else if (StringUtils.hasText(model.getText())) {
            ResultContent<DataSetModel> ret = dataSetService.push(model.getText());
            if (ret.getState() != ResultState.Success) {
                return ResultContent.build(ret.getState());
            }
            hash = ret.getContent().getHash();
        }

        //判断用户数据是否已存在
        if (userDataDao.existsByPlatformAndUserAndContentHash(platform, user, hash)) {
            return ResultContent.build(ResultState.UserDataSetExists);
        }

        //插入数据
        UserData userData = userDataDao.append(platform, user, hash);

        if (userData != null) {
            updateSearchIndex(userData);
        }

        //加入用户数据
        return userData == null ? ResultContent.build(ResultState.Fail) : ResultContent.build(ResultState.Success, userData.getId());
    }

    /**
     * 转换到模型
     *
     * @param document
     * @return
     */
    public UserData toModel(Document document) {
        UserData userData = JsonUtil.toObject(dbHelper.toJson(document), UserData.class);
        userData.setId(document.getObjectId("_id").toHexString());
        return userData;
    }

    /**
     * 同步索引到es中
     */
    private void updateSearchIndex(UserData userData) {
        this.userDataIndexService.update(userData);
    }

}
