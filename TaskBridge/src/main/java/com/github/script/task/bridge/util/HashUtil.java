package com.github.script.task.bridge.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class HashUtil {

    /**
     * 多文本hash工具
     *
     * @param texts
     * @return
     */
    public static String hash(String... texts) {
        StringBuilder sb = new StringBuilder();
        for (String text : texts) {
            sb.append(text + "_");
        }
        if (sb.length() > 0) {
            sb = sb.delete(sb.length() - 1, sb.length());
        }
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes(StandardCharsets.UTF_8));
    }


}
