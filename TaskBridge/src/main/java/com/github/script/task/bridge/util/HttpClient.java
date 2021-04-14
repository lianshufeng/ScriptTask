package com.github.script.task.bridge.util;

import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpHeaders;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class HttpClient {


    private final static String DefaultUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36";
    private final static String DefaultCharset = "UTF-8";


    //持久化的cookies信息
    private Map<String, String> cookies = new HashMap<String, String>();


    /**
     * 写入Cookies
     *
     * @param cookies
     */
    public void setCookies(String cookies) {
        for (String item : cookies.trim().split("; ")) {
            int at = item.trim().indexOf("=");
            if (at > -1) {
                this.cookies.put(item.substring(0, at).trim(), item.substring(at + 1, item.length()).trim());
            }
        }
    }

    /**
     * 读取Cookies
     *
     * @return
     */
    public String getCookies() {
        StringBuilder sb = new StringBuilder();
        for (String key : this.cookies.keySet()) {
            sb.append(key + "=" + this.cookies.get(key) + "; ");
        }
        return sb.toString();
    }


    @SneakyThrows
    protected Result request(HttpClientUtil.HttpModel httpModel) {
        Result result = new Result();
        result.headers = new HashMap<>();
        @Cleanup ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        result.code = HttpClientUtil.request(httpModel, byteArrayOutputStream, result.headers);
        result.bin = byteArrayOutputStream.toByteArray();

        //cookies持久化到内存
        Optional.ofNullable(result.headers.get("Set-Cookie")).ifPresent((it) -> {
            it.stream().map((setCookie) -> {
                return String.valueOf(setCookie);
            }).forEach((cookie) -> {
                int at = cookie.indexOf(";");
                if (at == -1) {
                    return;
                }
                String items = cookie.substring(0, at).trim();
                at = items.indexOf("=");
                if (at == -1) {
                    return;
                }
                this.cookies.put(items.substring(0, at), items.substring(at + 1, items.length()));
            });
        });

        return result;
    }


    /**
     * Get请求
     *
     * @param url
     * @return
     */
    public Result get(final String url) {
        return get(url, new HashMap<>(), DefaultCharset);
    }


    public Result get(final String url, Map<String, Object> headers, String charset) {
        //设置默认的请求头
        setDefaultRequestHeader(url, headers);

        HttpClientUtil.HttpModel httpModel = new HttpClientUtil.HttpModel();
        httpModel.setUrl(url);
        httpModel.setCharset(charset);
        httpModel.setHeader(headers);
        httpModel.setMethod(HttpClientUtil.MethodType.Get);

        return request(httpModel);
    }


    /**
     * 表单
     *
     * @param url
     * @return
     */
    public Result form(final String url, final Object parameter) {
        return form(url, parameter, new HashMap<>(), DefaultCharset);
    }


    /**
     * 提交表单
     *
     * @param url
     * @param parameter
     * @param headers
     * @return
     */
    public Result form(final String url, final Object parameter, final Map<String, Object> headers, final String charset) {
        //设置默认的请求头
        setDefaultRequestHeader(url, headers);

        HttpClientUtil.HttpModel httpModel = new HttpClientUtil.HttpModel();
        httpModel.setUrl(url);
        httpModel.setCharset(charset);
        httpModel.setHeader(headers);
        httpModel.setMethod(HttpClientUtil.MethodType.Post);
        httpModel.setBody(parameter);

        return request(httpModel);
    }


    public Result json(String url, Object parameter) {
        return json(url, parameter, new HashMap<>(), DefaultCharset);
    }


    public Result json(String url, Object parameter, Map<String, Object> headers, final String charset) {
        //设置默认的请求头
        setDefaultRequestHeader(url, headers);

        HttpClientUtil.HttpModel httpModel = new HttpClientUtil.HttpModel();
        httpModel.setUrl(url);
        httpModel.setCharset(charset);
        httpModel.setHeader(headers);
        httpModel.setMethod(HttpClientUtil.MethodType.Json);
        httpModel.setBody(parameter);

        return request(httpModel);
    }


    /**
     * 设置默认的请求头
     *
     * @param headers
     */
    @SneakyThrows
    private void setDefaultRequestHeader(String url, Map<String, Object> headers) {

        //设置Aceept
        if (!headers.containsKey(HttpHeaders.ACCEPT)) {
            headers.put(HttpHeaders.ACCEPT, "*/*");
        }

        //设置默认的 UA
        if (!headers.containsKey(HttpHeaders.USER_AGENT)) {
            headers.put(HttpHeaders.USER_AGENT, DefaultUserAgent);
        }

        //设置默认的 REFERER
        if (!headers.containsKey(HttpHeaders.REFERER)) {
            headers.put(HttpHeaders.REFERER, new URL(url).toExternalForm());
        }

        //加载持久化的Cookie
        if (!headers.containsKey(HttpHeaders.COOKIE) && this.cookies.size() > 0) {
            headers.put(HttpHeaders.COOKIE, getCookies());
        }


    }


    public static class Result {

        //数据返回的实体
        @Setter
        @Getter
        protected int code;

        //响应头
        @Setter
        @Getter
        protected Map<String, Set<Object>> headers;

        //数据
        @Setter
        @Getter
        protected byte[] bin;

        /**
         * 转换对象
         *
         * @return
         */
        public Parse parse() {
            return parse("UTF-8");
        }

        @SneakyThrows
        public Parse parse(String charset) {
            Parse parse = new Parse();
            parse.buffer = new String(bin, charset);
            return parse;
        }


        public static class Parse {

            //数据
            protected String buffer = "";

            /**
             * 直接取出文本
             *
             * @return
             */
            public String text() {
                return buffer;
            }


            /**
             * 转换为Dom元素
             *
             * @return
             */
            public Document html() {
                return Jsoup.parse(buffer);
            }


            /**
             * 转换为JSON对象
             *
             * @return
             */
            public Object json() {
                return json(Object.class);
            }

            @SneakyThrows
            public <T> T json(Class<T> cls) {
                return JsonUtil.toObject(buffer, cls);
            }


        }


    }


}
