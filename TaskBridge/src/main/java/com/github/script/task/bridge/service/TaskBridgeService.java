package com.github.script.task.bridge.service;

import com.github.script.task.bridge.conf.ScriptTaskConf;
import com.github.script.task.bridge.util.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.github.script.task.bridge.util.BeanUtil;
import com.github.script.task.bridge.util.JsonUtil;

import java.util.Map;

@Service
public class TaskBridgeService {

    //地址
    @Autowired
    private ScriptTaskConf scriptTaskConf;

    //请求地址
    private RestTemplate restTemplate = new RestTemplate();


    /**
     * 表单通信
     *
     * @param <T>
     * @return
     */
    public <T> T postForm(String uri, Object parameter, Class<T> retCls) {
        return (T) new HttpClient().form(url(uri), parameter).parse().json(retCls);
    }

    /**
     * Json通信
     *
     * @return
     */
    public <T> T postJson(String uri, Object parameter, Class<T> retCls) {
        return (T) new HttpClient().json(url(uri), parameter).parse().json(retCls);
    }

    private String url(String uri) {
        return this.scriptTaskConf.getHost() + "/" + uri;
    }
}
