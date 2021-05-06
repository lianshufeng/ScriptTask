package com.github.script.task.server.core.service;

import com.baidu.aip.nlp.AipNlp;
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
import java.util.regex.Pattern;

@Slf4j
@Service
public class NLPService {

    @Autowired
    private NLPConf nlpConf;

    //中文正则表达式
    final static Pattern ChinesePattern = Pattern.compile("[(a-zA-Z0-9\\u4e00-\\u9fa5)]");

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
        final String ret = format(text);
        return ret.length() > length ? getClient().newsSummary(ret, length, new HashMap<>()).getString("summary") : ret;
    }


    /**
     * 取关键词
     *
     * @param text
     * @return
     */
    public String[] keyword(String text, String title) {
        return getTags(getClient().keyword(format(title), format(text), new HashMap<>()).getJSONArray("items"));
    }


    /**
     * 取出主题标签
     *
     * @param text
     * @return
     */
    public TopicModel topic(String text, String title) {
        TopicModel topicModel = new TopicModel();
        JSONObject item = getClient().topic(format(title), format(text), new HashMap<>()).getJSONObject("item");
        topicModel.setLv1_tag_list(getTags(item.getJSONArray("lv1_tag_list")));
        topicModel.setLv2_tag_list(getTags(item.getJSONArray("lv2_tag_list")));
        return topicModel;

    }


    /**
     * 格式化中文
     *
     * @return
     */
    private String format(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            String str = text.substring(i, i + 1);
            sb.append(ChinesePattern.matcher(str).find() ? str : " ");
        }
        return sb.toString();
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
