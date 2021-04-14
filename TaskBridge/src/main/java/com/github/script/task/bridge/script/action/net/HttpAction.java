package com.github.script.task.bridge.script.action.net;

import com.github.script.task.bridge.conf.WebFetchConf;
import com.github.script.task.bridge.script.SuperScriptAction;
import com.github.script.task.bridge.util.HttpClient;
import com.github.script.task.bridge.util.HttpClientUtil;
import com.github.script.task.bridge.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 网络请求，支持 Get post form , 支持自动维护cookies , 以及Dom JSON 的处理
 */
@Slf4j
public class HttpAction extends SuperScriptAction {

    @Autowired
    private WebFetchConf webFetchConf;

    //是否启用代理
    @Getter
    @Setter
    private boolean proxy = false;

    static long accessCount = 0;

    /**
     * 计次访问
     */
    private synchronized String getProxyUrl() {
        int index = Integer.parseInt(String.valueOf(accessCount % webFetchConf.getUrl().length));
        accessCount++;
        return webFetchConf.getUrl()[index];
    }


    @Delegate(types = HttpClient.class)
    private HttpClient httpClient = new HttpClient() {
        /**
         * hook并修改源方法
         * @param httpModel
         * @return
         */
        @Override
        protected Result request(HttpClientUtil.HttpModel httpModel) {
            //如果不使用代理则直接访问
            if (proxy == false || webFetchConf.getUrl() == null || webFetchConf.getUrl().length == 0) {
                return super.request(httpModel);
            }
            return proxyRequest(httpModel);
        }

        /**
         * 直接访问
         * @param httpModel
         * @return
         */
        public Result direct(HttpClientUtil.HttpModel httpModel) {
            return super.request(httpModel);
        }
    };


    /**
     * 代理访问
     *
     * @param httpModel
     * @return
     */
    @SneakyThrows
    private HttpClient.Result proxyRequest(HttpClientUtil.HttpModel httpModel) {
        //根据系统时间选举一个代理服务器
        String proxyServer = getProxyUrl();

        final HttpClientUtil.HttpModel newHttpModel = new HttpClientUtil.HttpModel();
        newHttpModel.setUrl(proxyServer);
        newHttpModel.setMethod(HttpClientUtil.MethodType.Json);
        newHttpModel.setBody(new HashMap<String, Object>() {{
            put("url", httpModel.getUrl());

            Optional.ofNullable(httpModel.getMethod()).ifPresent((it) -> {
                put("method", (it == HttpClientUtil.MethodType.Post || it == HttpClientUtil.MethodType.Json) ? "POST" : "GET");
            });

            Optional.ofNullable(httpModel.getCharset()).ifPresent((it) -> {
                put("charset", it);
            });

            Optional.ofNullable(httpModel.getHeader()).ifPresent((it) -> {
                put("headers", it);
            });

            //非空不用传
            Optional.ofNullable(httpModel.getBody()).ifPresent((it) -> {
                put("body", it instanceof String ? it : JsonUtil.toJson(it));
            });

        }});

        log.info("{} -> {}", proxyServer, httpModel.getUrl());

        //反射直接访问父方法
        Method method = httpClient.getClass().getMethod("direct", HttpClientUtil.HttpModel.class);
        HttpClient.Result result = (HttpClient.Result) method.invoke(httpClient, newHttpModel);
        Map<String, Object> resultMap = JsonUtil.toObject(new String(result.getBin()), Map.class);
        HttpClient.Result ret = new HttpClient.Result();
        ret.setCode(Integer.parseInt(String.valueOf(resultMap.get("status"))));
        ret.setHeaders((Map) resultMap.get("headers"));
        ret.setBin(String.valueOf(resultMap.get("body")).getBytes(httpModel.getCharset()));


        return ret;
    }


}
