package com.github.script.task.server.core.service;

import com.baidu.aip.nlp.AipNlp;
import com.github.script.task.bridge.util.TextUtil;
import com.github.script.task.server.core.conf.NLPConf;
import com.github.script.task.server.core.model.nlp.TopicModel;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class NLPService {

    @Autowired
    private NLPConf nlpConf;


    private AipNlp[] aipNlps = null;
    private int incCount = 0;


    /**
     * 取出客户端
     *
     * @return
     */
    public synchronized AipNlp getClient() {
        if (aipNlps == null || aipNlps.length == 0) {
            return null;
        }
        //防止int溢出
        if (incCount < 0) {
            incCount = 0;
        }
        return aipNlps[(incCount++) % aipNlps.length];
    }

    @Autowired
    private void initClient(ApplicationContext applicationContext) {
        List<AipNlp> aipNlps = new ArrayList<>();
        for (NLPConf.App app : nlpConf.getApp()) {
            aipNlps.add(new AipNlp(app.getAppId(), app.getApiKey(), app.getSecretKey()));
        }
        this.aipNlps = aipNlps.toArray(new AipNlp[0]);
    }


    /**
     * 提取数据摘要
     */
    public String summary(String text, int length) {
        final String ret = TextUtil.format(text);
        return ret.length() > length ? getClient().newsSummary(ret, length, new HashMap<>()).getString("summary") : ret;
    }


    /**
     * 取关键词
     *
     * @param text
     * @return
     */
    public String[] keyword(String text, String title) {
        return getTags(getClient().keyword(TextUtil.format(title), TextUtil.format(text), new HashMap<>()).getJSONArray("items"));
    }


    /**
     * 取出主题标签
     *
     * @param text
     * @return
     */
    public TopicModel topic(String text, String title) {
        TopicModel topicModel = new TopicModel();
        JSONObject item = getClient().topic(TextUtil.format(title), TextUtil.format(text), new HashMap<>()).getJSONObject("item");
        topicModel.setLv1_tag_list(getTags(item.getJSONArray("lv1_tag_list")));
        topicModel.setLv2_tag_list(getTags(item.getJSONArray("lv2_tag_list")));
        return topicModel;

    }


    /**
     * 取标签
     *
     * @param jsonArray
     * @return
     */
    private String[] getTags(JSONArray jsonArray) {
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            tags.add(jsonObject.getString("tag"));
        }
        return tags.toArray(new String[0]);
    }


}
