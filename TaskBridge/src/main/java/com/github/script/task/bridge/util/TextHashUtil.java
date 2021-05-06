package com.github.script.task.bridge.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class TextHashUtil {

    /**
     * 文本摘要
     *
     * @param text
     * @return
     */
    public static String hash(String text) {
        if (text == null) {
            return null;
        }
        return DigestUtils.md5Hex(text.getBytes(StandardCharsets.UTF_8)).toLowerCase(Locale.ROOT);
    }


}
