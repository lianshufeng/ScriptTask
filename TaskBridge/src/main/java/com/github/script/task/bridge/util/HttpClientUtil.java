package com.github.script.task.bridge.util;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.util.StreamUtils;
import org.springframework.validation.annotation.Validated;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

@Slf4j
public class HttpClientUtil {


    /**
     * 网络请求
     *
     * @param httpModel
     * @return
     * @throws IOException
     */
    public static ResponseModel request(HttpModel httpModel) throws IOException {
        Map<String, Set<Object>> headers = new HashMap<>();
        @Cleanup ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Integer code = request(httpModel, byteArrayOutputStream, headers);
        byte[] content = byteArrayOutputStream.toByteArray();
        String body = getBody(headers, content);
        return ResponseModel.builder().headers(headers).body(body).code(code).build();
    }


    /**
     * 网络请求
     *
     * @param httpModel
     * @param outputStream
     * @return
     */
    @SneakyThrows
    public static Integer request(HttpModel httpModel, OutputStream outputStream, Map<String, Set<Object>> headers) throws IOException {
        //构建超时参数
        RequestConfig.Builder builder = RequestConfig.custom();
        if (httpModel.getTimeOut() != null) {
            builder.setSocketTimeout(httpModel.getTimeOut());
        }
        RequestConfig requestConfig = builder.build();

        //忽略ssl
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        }).build();
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();


        HttpRequestBase requestBase = null;
        String[] urls = httpModel.getUrl().split("://");

        String url = urls[0] + "://" + UrlEncodeUtil.encode(urls[1]);

        //请求类型的判断
        if (httpModel.getMethod() == MethodType.Get || httpModel.getMethod() == null) {
            requestBase = new HttpGet(url);
        } else if (httpModel.getMethod() == MethodType.Post || httpModel.getMethod() == MethodType.Json) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(buildHttpEntity(httpModel));
            requestBase = httpPost;
        }

        //设置请求头
        if (httpModel.getHeader() != null) {
            for (Map.Entry<String, Object> entry : httpModel.getHeader().entrySet()) {
                requestBase.setHeader(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }


        //开始请求
        CloseableHttpResponse response = httpclient.execute(requestBase);

        if (headers != null) {
            for (Header header : response.getAllHeaders()) {
                Set<Object> val = headers.get(header.getName());
                if (val == null) {
                    val = new HashSet<>();
                    headers.put(header.getName(), val);
                }
                val.add(header.getValue());
            }
        }

        //转发数据流
        @Cleanup InputStream inputStream = response.getEntity().getContent();
        int size = StreamUtils.copy(inputStream, outputStream);
        log.debug("requestUrl : " + httpModel.getUrl() + " , responseSize : " + size);
        return response.getStatusLine().getStatusCode();
    }


    /**
     * 获取body部分
     *
     * @param headers
     * @param content
     * @return
     */
    @SneakyThrows
    public static String getBody(Map<String, Set<Object>> headers, byte[] content) {
        Set<Object> contentTypeHeader = headers.get("Content-Type");


        String contentType = null;
        String charset = null;
        if (contentTypeHeader != null && contentTypeHeader.size() > 0) {
            String val = String.valueOf(contentTypeHeader.toArray(new Object[0])[0]);
            String[] tmp = val.split(";");
            if (tmp.length > 0) {
                contentType = tmp[0];
            }
            if (tmp.length > 1) {
                charset = tmp[1];
            }
        }

        //获取字符编码
        if (charset != null) {
            String[] charsetArr = charset.split("=");
            if (charset.length() > 0) {
                charset = charsetArr[1];
            }
        }

        return new String(content, charset == null ? "UTF-8" : charset);
    }


    /**
     * 创建数据
     *
     * @param httpModel
     * @return
     */
    private static StringEntity buildHttpEntity(HttpModel httpModel) {
        String body = null;
        String mimeType = null;
        if (httpModel.getMethod() == MethodType.Post) {
            mimeType = "application/x-www-form-urlencoded";
            body = toFormText(httpModel.getBody());
        } else if (httpModel.getMethod() == MethodType.Json) {
            mimeType = "application/json";
            body = toJsonText(httpModel.getBody());
        }
        return new StringEntity(body, ContentType.create(mimeType, httpModel.getCharset()));
    }


    /**
     * 转换为JSON文本
     *
     * @return
     */
    private static String toJsonText(Object parameter) {
        return parameter == null ? "{}" : JsonUtil.toJson(parameter);
    }

    private static String toFormText(Object parameter) {
        //对象转换为表单
        Map<String, Object> param = null;
        if (parameter instanceof Map) {
            param = (Map) parameter;
        } else {
            param = BeanUtil.toMap(parameter);
        }

        StringBuilder sb = new StringBuilder();
        param.entrySet().forEach((it) -> {
            sb.append(UrlEncodeUtil.encode(it.getKey()) + "=" + UrlEncodeUtil.encode(String.valueOf(it.getValue())));
            sb.append("&");
        });

        //删除最后一个字符
        if (param.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }

        return sb.toString();
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Validated
    public static class HttpModel {


        /**
         * Url地址
         */
        private String url;


        /**
         * 网络请求方式
         */
        private MethodType method;


        /**
         * 请求头
         */
        private Map<String, Object> header;


        /**
         * 请求体，仅为post生效
         */
        private Object body;


        /**
         * 请求编码
         */
        private String charset;


        /**
         * 超时
         */
        private Integer timeOut;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseModel {

        /**
         * 响应编码
         */
        private int code;


        /**
         * header
         */
        private Map<String, Set<Object>> headers;

        /**
         * body
         */
        private String body;

    }

    public static class UrlEncodeUtil {

        private final static String[] NotEncodeCharset = new String[]{
                ":", "/", "\\", "&", "?", "="
        };
        private final static Set<Integer> NotEncodeAscii = Collections.synchronizedSet(new HashSet<>());

        static {
            for (String s : NotEncodeCharset) {
                try {
                    NotEncodeAscii.add((int) s.getBytes("UTF-8")[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @SneakyThrows
        public static String encode(String url) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < url.length(); i++) {
                String str = url.substring(i, i + 1);
                int ascii = (int) str.getBytes("UTF-8")[0];
                // 0 - 9  , A - Z , a = z , ( NotEncodeCharset  )
                if ((ascii >= 48 && ascii <= 57) || (ascii >= 65 && ascii <= 90) || (ascii >= 97 && ascii <= 122) || (NotEncodeAscii.contains(ascii))) {
                    sb.append(str);
                } else {
                    sb.append(URLEncoder.encode(str, "UTF-8"));
                }
            }

            return sb.toString();
        }

    }

    /**
     * 方法类型
     */
    public enum MethodType {
        Post,
        Get,
        Json
    }


}
