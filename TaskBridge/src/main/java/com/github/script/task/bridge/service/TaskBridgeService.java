package com.github.script.task.bridge.service;

import com.github.script.task.bridge.conf.ScriptTaskConf;
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //对象转换为表单
        Map<String, Object> param = null;
        if (parameter instanceof Map) {
            param = (Map) parameter;
        } else {
            param = BeanUtil.toMap(parameter);
        }
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        param.entrySet().forEach((it) -> {
            params.add(it.getKey(), String.valueOf(it.getValue()));
        });

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        return (T) this.restTemplate.postForEntity(url(uri), requestEntity, retCls);
    }

    /**
     * Json通信
     *
     * @return
     */
    public <T> T postJson(String uri, Object parameter, Class<T> retCls) {
        String json = JsonUtil.toJson(parameter);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON.toString() + "; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<Object> formEntity = new HttpEntity<>(parameter, headers);
        return (T) restTemplate.postForObject(url(uri), formEntity, retCls);
    }

    private String url(String uri) {
        return this.scriptTaskConf.getHost() + "/" + uri;
    }
}
